package model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
//import jakarta.servlet.jsp.JspWriter;
//import jakarta.servlet.jsp.PageContext;

public class FileUtils {
	// [파일 이름 중복 체크용 메소드]
	/**
	 * 중복파일이 있을경우 인덱싱 번호를 추가해서 새로운 파일명을 반환하는 메소드
	 * @param path  파일이 저장된 서버의 물리적 경로
	 * @param fileName 업로드한 파일명
	 * @return
	 */
	public static String getNewFileName(String path, String fileName) {
		// fileName=원격.txt
		File file = new File(path + File.separator + fileName);
		if (!file.exists()) {
			return fileName;
		} else {
			String ext = fileName.substring(fileName.lastIndexOf(".") + 1).trim();
			String fileNameExcludeExt = fileName.substring(0, fileName.lastIndexOf("."));

			String newFileName;
			while (true) {
				newFileName = "";
				if (fileNameExcludeExt.indexOf("_") != -1) {// 파일명에 _가 포함됨
					String[] arrFiles = fileNameExcludeExt.split("_");
					String lastName = arrFiles[arrFiles.length - 1];
					try {
						int index = Integer.parseInt(lastName);
						for (int i = 0; i < arrFiles.length; i++) {
							if (i != arrFiles.length - 1)
								newFileName += arrFiles[i] + "_";
							else
								newFileName += String.valueOf(index + 1);
						}
						newFileName += "." + ext;
					} catch (Exception e) {
						newFileName += fileNameExcludeExt + "_1." + ext;
					}
				} else {// _가 없음
					newFileName += fileNameExcludeExt + "_1." + ext;
				}
				File f = new File(path + File.separator + newFileName);
				if (f.exists()) {
					fileNameExcludeExt = newFileName.substring(0, newFileName.lastIndexOf("."));
					continue;
				} else
					break;
			} //////////// while

			return newFileName;
		}
	}/////////////////////
	/**
	 * 다중 파일 업로드용 메소드로 파일을 하나라도 업드시는 파일명이 저장된 StringBuffer반환
	 * 미 업로드시는 null반환
	 * 
	 * @param parts Collection<Part>으로 업로드된 파라미터 정보를 갖고있는 Part객체 컬렉션
	 * @param saveDirectory 파일이 저장된 서버의 물리적경로
	 * @return 파일명을 저장한 StringBuffer
	 */
	//업로드 로직
	
	public static StringBuffer upload(Collection<Part> parts,String saveDirectory) {
		
		//업로드된 파일명들 저장용
		StringBuffer fileNames = new StringBuffer();
		try {			
			for(Part part:parts) {
				
				if (part.getContentType() != null && !part.getName().equals("thumbnail")) {//input type="file"인 경우				
					String systemFileName=getNewFileName(saveDirectory, part.getSubmittedFileName());					
					//파일 업로드
					part.write(saveDirectory+File.separator+systemFileName);
					//파일명을 스프링버퍼에 저장
					fileNames.append(systemFileName+",");//구분자			
				}				
			}
		}
		catch(Exception e) {//파일 미첨부시 에러:null반환			
			return null;
		}
		return fileNames.deleteCharAt(fileNames.length()-1);
	}
	
    // 썸네일 업로드 로직
    public static String uploadThumbnail(Part thumbnailPart, String thumbnailDirectory) throws IOException {
        String thumbnailFileName = "";
        if (thumbnailPart != null && thumbnailPart.getSize() > 0) {
            thumbnailFileName = Paths.get(thumbnailPart.getSubmittedFileName()).getFileName().toString();
            File thumbnailFile = new File(thumbnailDirectory + File.separator + thumbnailFileName);
            thumbnailPart.write(thumbnailFile.getAbsolutePath());
            System.out.println("Uploaded Thumbnail: " + thumbnailFile.getAbsolutePath());
        }
        return thumbnailFileName;
    }

	/**
	 * 파일 삭제용 메소드
	 * 
	 * 
	 * @param fileNames 업로드한 모든 파일명이 저장된 스트링버퍼
	 * @param saveDirectory 파일이 저장된 서버의 물리적경로
	 */
	//삭제 로직
    public static void delete(StringBuffer fileNames, String saveDirectory, String delim) {
        String[] files = fileNames.toString().split(delim);
        for (String filename : files) {
            File f = new File(saveDirectory + File.separator + filename);
            if (f.exists()) f.delete();
        }
	}////////////////////////////////////////
	/**
	 * 서버에 있는 파일 다운로드용 메소드
	 * @param filename 다운로드할 파일명
	 * @param webPath  파일이 저장된 서버의 웹상의 경로(wepapp아래)
	 * @param request HttpServletRequest객체
	 * @param response HttpServletResponse객체
	 */
	//다운로드 로직
	public static void download(String filename,String webPath,HttpServletRequest request,HttpServletResponse response) {	
		
		try {
			String saveDirectory=request.getServletContext().getRealPath(webPath);		
			File file = new File(saveDirectory+File.separator+filename);
			long length=file.length();		
			response.setContentType("application/octect-stream");		
			response.setContentLengthLong(length);		
			boolean isIe = request.getHeader("user-agent").toUpperCase().indexOf("MSIE") != -1 ||
					       request.getHeader("user-agent").toUpperCase().indexOf("11.0") != -1 ||
					       request.getHeader("user-agent").toUpperCase().indexOf("EDGE") != -1;
			if(isIe) //인터넷 익스플로러 혹은 엣지
				filename = URLEncoder.encode(filename, "UTF-8");
			else{//기타 브라우저			
		 		filename = new String(filename.getBytes("UTF-8"),"8859_1"); 		
			}
			response.setHeader("Content-Disposition","attachment;filename="+filename);
			
			//5]서버에 있는 파일에 연결할 입력 스트림 생성
			BufferedInputStream bis=
				new BufferedInputStream(new FileInputStream(file));
			
			
			//6]웹브라우저에 연결할 출력 스트림 생성
			BufferedOutputStream bos =
				new BufferedOutputStream(response.getOutputStream());
			//7]bis로 읽고 bos로 웹브라우저에 출력
			int data;
			while((data=bis.read()) !=-1){
				bos.write(data);
				bos.flush();
			}
			//8]스트림 닫기
			bis.close();
			bos.close();
		}
		catch(Exception e) {e.printStackTrace();}
	}/////////////////
	
    // 파일을 byte[]로 변환하는 유틸리티 메서드
	public static byte[] fileToByteArray(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, bytesRead, bytesRead);
            }
            return bos.toByteArray();
        }
    }
    // 파일을 Blob으로 변환하는 유틸리티 메서드
	public static Blob createBlob(File file) throws IOException, SerialException {
        byte[] fileContent = fileToByteArray(file);
        try {
            return new SerialBlob(fileContent);
        } catch (SQLException e) {
            throw new IOException("Failed to create Blob from file content", e);
        }
    }
}
