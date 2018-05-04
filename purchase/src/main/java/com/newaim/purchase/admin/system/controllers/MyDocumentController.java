package com.newaim.purchase.admin.system.controllers;

import com.google.common.collect.Maps;
import com.newaim.core.beans.NoSupportExtensionException;
import com.newaim.core.beans.OutOfSizeException;
import com.newaim.core.contoller.ControllerBase;
import com.newaim.core.utils.*;
import com.newaim.purchase.admin.account.vo.UserVo;
import com.newaim.purchase.admin.system.entity.MyDocument;
import com.newaim.purchase.admin.system.service.MyDocumentCategoryService;
import com.newaim.purchase.admin.system.service.MyDocumentService;
import com.newaim.purchase.admin.system.vo.MyDocumentVo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/mydoc")
public class MyDocumentController extends ControllerBase{

	@Autowired
    private MyDocumentService myDocumentService;
	
	@Autowired
	private MyDocumentCategoryService myDocumentCategoryService;

	/** 上传目录名 */
	private static final String uploadFolderName =  FileConvertUtils.uploadFolderName;

	/** 上传临时文件存储目录 */
	private static final String tempFolderName = "var/temp/";

	/** 上传文件最大为30M */
	private static final Long fileMaxSize = 300000000L;

	/** 统一的编码格式 */
	private static final String encode = "UTF-8";

	@ResponseBody
	@RequiresPermissions("MyDocument:normal:list")
    @PostMapping("/list")
    public RestResult list(ServletRequest request, String sort, String keywords, String type,
    		@RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize
     ){
		RestResult result = new RestResult();
    	try {
    		LinkedHashMap<String, Object> params = Maps.newLinkedHashMap();

			if(StringUtils.isNotBlank(keywords)){
				params.put("path-S-LK-OR", keywords);
				params.put("name-S-LK-OR", keywords);
				params.put("creatorCnName-S-LK-OR", keywords);
				params.put("creatorEnName-S-LK-OR", keywords);
				params.put("extension-S-LK-OR", keywords);
				params.put("note-S-LK-OR", keywords);
				params.put("departmentCnName-S-LK-OR", keywords);
				params.put("departmentEnName-S-LK-OR", keywords);
			}else{
				params = ServletUtils.getParametersStartingWith(request);
				//搜索出指定部门以下的所有记录
				if(params.size()>0){
					if(params.get("departmentId-S-EQ") != null && StringUtils.isNotBlank(params.get("departmentId-S-EQ").toString())){
						String depIds = getDepartmentsByDepId(params.get("departmentId-S-EQ").toString());
						params.remove("departmentId-S-EQ");
						params.put("departmentId-S-IN", depIds);
					}
				}
			}

			if(StringUtils.isNotEmpty(type)){
				params.put("type-N-EQ-ADD", type);
			}

			params = setParams(params, "MyDocument", ":4", false);

            if(hasDataType("MyDocument:3")){

            }else if(hasDataType("MyDocument:2")){
                //部门内
                String depIds = getMyDepartments();
                params.remove("departmentId-S-EQ");
                params.put("departmentId-S-IN-OR", depIds);
                //可看到分享的所有人范围
                params.put("shared-N-EQ-OR", "3");
            }else{
                //自身
                UserVo user = SessionUtils.currentUserVo();
                params.put("creatorId-S-EQ-OR", user.getId());
                //可看到分享的所有人范围
                params.put("shared-N-EQ-OR", "3");
            }

			//sort 字段转换
            Map<String, String> sortMap = Maps.newHashMap();
            sortMap.put("category.title","categoryId");

    		Page<MyDocumentVo> rd = myDocumentService.list(params, pageNumber, pageSize, getSort(sort, sortMap));
    		
    		result.setSuccess(true).setPage(rd).setMsg(localeMessageSource.getMsgListSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgListFailure(e.getMessage()));
		}
    	return result;
    }

	@ResponseBody
	@RequiresPermissions("MyDocument:normal:list")
	@PostMapping("/get")
	public RestResult get(String id) {
		RestResult result = new RestResult();
		try {
			MyDocumentVo rd =  myDocumentService.get(id);
			
			result.setSuccess(true).setData(rd).setMsg(localeMessageSource.getMsgGetSuccess());
			
		} catch (Exception e) {
			result.setSuccess(false).setMsg(localeMessageSource.getMsgGetFailure(e.getMessage()));
		}
		
		return result; 
	}

	@ResponseBody
	@RequiresPermissions(value = {"MyDocument:normal:add", "MyDocument:normal:edit"}, logical = Logical.OR)
	@PostMapping("/save")
	public RestResult save(ServletRequest request, @RequestParam(value = "files") MultipartFile[] files, String act, @ModelAttribute("main") MyDocument main) {
		RestResult result = new RestResult();
		try {
			//仅修改自己的记录
			UserVo user = SessionUtils.currentUserVo();
			if(StringUtils.isNotBlank(main.getId()) && !main.getCreatorId().equals(user.getId())){
				throw new Exception(localeMessageSource.getMsgUnauthorized());
			}
			
			//获取分类子目录名
			String catePath = "";
			if(main.getCategoryId() != null && StringUtils.isNotBlank(main.getCategoryId())){
				catePath = myDocumentCategoryService.get(main.getCategoryId()).getPath();
			}else{
				catePath = "others";
			}
			
			//文件上传
			for (MultipartFile mf : files) {  
                if(!mf.isEmpty()){
                	String newFileName = "";
        			String fileExtension = "";
        			UserVo curUser = SessionUtils.currentUserVo();
        			Calendar now = Calendar.getInstance();
        			int year = now.get(Calendar.YEAR);
        			int month = now.get(Calendar.MONTH) + 1;
        			String dbFilePath = curUser.getId() + "/" + catePath + "/"+ year + month + "/";
        			String filePath = uploadFolderName + dbFilePath;
        			request.removeAttribute("fileUploadProcess");
        			
        			String curProjectPath = request.getServletContext().getRealPath("/");
        			String saveDirectoryPath = curProjectPath + "/" + filePath;
        			
                	this._markDir(saveDirectoryPath);
        			File saveDirectory = new File(saveDirectoryPath);
        			
                	fileExtension = FilenameUtils.getExtension(mf.getOriginalFilename());
                	if (!ArrayUtils.contains(FileConvertUtils.extPermit, fileExtension.toLowerCase())) {
						throw new NoSupportExtensionException("不支持文件类型");
					}
                	if (mf.getSize() > fileMaxSize) {
                		throw new OutOfSizeException("文件超出上传限制大小");
                	}
                	newFileName = MD5Util.md5(year + month + now.get(Calendar.DAY_OF_MONTH) + now.getTimeInMillis() + "")	+ "." + fileExtension;
					FileUtils.copyInputStreamToFile(mf.getInputStream(), new File(saveDirectory, newFileName));
                	main.setBytes(mf.getSize());
                	main.setName(mf.getOriginalFilename());
                	main.setExtension(fileExtension);
                	main.setType(FileUtil.getFileCatByExt(main.getExtension()));
                	main.setPath(filePath + newFileName);

                }  
            }
			
			if(main.getId() != null && StringUtils.isNotBlank(main.getId())){
				if(StringUtils.isNotBlank(act) && ACT_COPY.equals(act)){
					//复制另存
					myDocumentService.saveAs(main);
				}else{
					//保存
					myDocumentService.save(main);
				}
			}else{
				//新建
				myDocumentService.add(main);
			}
			
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgSaveSuccess(act));
			
		} catch (OutOfSizeException e) {
			result.setSuccess(false).setData(null).setMsg(e.getMessage());
		} catch (NoSupportExtensionException e) {
			result.setSuccess(false).setData(null).setMsg(e.getMessage());
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgSaveFailure("save", e.getMessage()));
		}finally {
			request.removeAttribute("fileUploadProcess");
		}
		return result; 
	}

	@ResponseBody
	@RequiresPermissions("MyDocument:normal:del")
	@PostMapping("/delete")
	public RestResult delete(ServletRequest request, String ids) {
		RestResult result = new RestResult();
		try {
			
			UserVo user = SessionUtils.currentUserVo();
			if(!myDocumentService.get(ids).getCreatorId().equals(user.getId())){
				throw new Exception(localeMessageSource.getMsgUnauthorized());
			}

			myDocumentService.delete(request, ids);
			
			result.setSuccess(true).setData(null).setMsg(localeMessageSource.getMsgDeleteSuccess());
		} catch (Exception e) {
			result.setSuccess(false).setData(null).setMsg(localeMessageSource.getMsgOperateException(e.getMessage()));
		}
		
		return result;
	}

	@GetMapping("/preview")
	public String preview(ServletRequest request, Model model, String fileId){
		String mark = "no";
		String fileUrl = "";
		int fileDisplayType = 0;

		if (StringUtils.isNotEmpty(fileId)) {
			FileConvertUtils fcu = new FileConvertUtils();

			UserVo user = SessionUtils.currentUserVo();
			MyDocument myDoc = myDocumentService.getMyDocument(fileId);

			model.addAttribute("lang", user.getLang());
			model.addAttribute("fileId", fileId);
			model.addAttribute("fileName", myDoc.getName());

			String curProjectPath = request.getServletContext().getRealPath("/");
			String filePath = myDoc.getPath();
			String saveDirectoryPath = curProjectPath + filePath;

			File file = new File(saveDirectoryPath);

			try {
				if( file.exists()){
					mark = "yes";
					String fileType = myDoc.getExtension();
					fileUrl = ("/" +myDoc.getPath()).replace("//", "/");

					if(fileType.equals("xls") || fileType.equals("xlsx")
							|| fileType.equals("doc") || fileType.equals("docx")
							|| fileType.equals("ppt") || fileType.equals("pptx") ){

						fileUrl = fcu.getOutputFilePath( myDoc.getPath(), "swf").replace("//", "/");
						mark = fcu.docToSwf(request, myDoc);
					}else{
						if(fileType.equals("jpg") || fileType.equals("gif")	|| fileType.equals("png") || fileType.equals("bmp")){
						}else if( fileType.equals("mp3") || fileType.equals("wma") || fileType.equals("wav")){
						}else if( fileType.equals("wmv") || fileType.equals("webm") || fileType.equals("mp4")){
						}else if( fileType.equals("pdf")){
							fileUrl = fcu.getOutputFilePath( fileUrl, "swf");
							mark = fcu.pdfToSwf(request, myDoc);
						}
					}

					if(mark.equals("no")) { throw new Exception("文件不存在！");}
					    fileDisplayType = FileUtil.getFileCatByExt(fileType);
					    String hostStr = (request.getProtocol().equals("HTTP/1.1")? "http://": "https://") + request.getServerName() + (request.getServerPort() == 80? "": ":"+request.getServerPort());
                        request.setAttribute("fileUrl", hostStr + (request.getServletContext().getContextPath()+"/"+fileUrl).replace("//", "/"));

						if (fileDisplayType == 2 || fileDisplayType == 5) {
							return "document/preview/office";
						} else if (fileDisplayType == 1) {
							return "document/preview/image";
						} else if (fileDisplayType == 3) {
							return "document/preview/audio";
						} else if (fileDisplayType == 4) {
							return "document/preview/video";
						} else {
							return "document/preview/download";
						}
				}else{
					logger.info("源文件不存!");
					return "document/preview/nofile";
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "document/preview/none";
	}

    @GetMapping("/download")
    public void download(ServletRequest request, HttpServletResponse response, String fileId, Boolean isOnLine) throws IOException {
	    try {
            //文件ID错误报404
            if (null == fileId || StringUtils.isEmpty(fileId)) {
                response.sendError(404, "File not found!");
                return;
            }

            //未登录时不允许下载报404
            UserVo user = SessionUtils.currentUserVo();
            if (null == user || null == user.getId() || StringUtils.isEmpty(user.getId())) {
                response.sendError(404, "File not found!");
                return;
            }

            //文件错误报404
            MyDocument myDoc = myDocumentService.getMyDocument(fileId);
            if (null == myDoc) {
                response.sendError(404, "File not found!");
                return;
            }

//            //禁止下载不是信息的文件
//            if(!user.getId().equals(myDoc.getCreatorId())){
//                response.sendError(404, "File not found!");
//                return;
//            }

            String path = request.getServletContext().getRealPath("/") + myDoc.getPath();
            //文件不存在时不报404
            File f = new File(path);
            if (!f.exists()) {
                response.sendError(404, "File not found!");
                return;
            }
            BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
            byte[] buf = new byte[1024];
            int len = 0;

            response.reset(); // 非常重要
            if (null != isOnLine && isOnLine) { // 在线打开方式
                // 文件名应该编码成UTF-8
            } else { // 纯下载方式
                response.setContentType("application/x-msdownload");
                response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(myDoc.getName(), "UTF-8").replace(" ", "_"));
            }
            OutputStream out = response.getOutputStream();
            while ((len = br.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            br.close();
            out.close();
        }catch (Exception e){
            response.sendError(404, "File not found!");
            return;
        }
    }
	
	@ModelAttribute("main")
    public MyDocument main(String act, String id){
        if(StringUtils.isNotBlank(act) && StringUtils.isNotBlank(id)){
            return myDocumentService.getMyDocument(id);
        }else if(StringUtils.isNotBlank(act)){
        	return new MyDocument();
        }
        return null;
    }
	
	private void _markDir(String path){
		String[] str = path.split("\\/");
		String curPath = "";
		if(str.length > 0){
			for(int i=0; i<str.length; i++){
				curPath += str[i] + "/";
				if(! new File(curPath).exists()){
					new File(curPath).mkdir();
				}
			}
		}
	}
}
