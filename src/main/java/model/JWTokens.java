package model;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class JWTokens {

	private static SecretKey scSecretKey;
	private static final String SCRETKEY_PATH="/resources/tokens";//확장자는 생략(.properties)
	private static final String KEY="secretKey";
	
	static {
		//비밀키가 저장된 파일에서 비밀키값 읽어오기
		ResourceBundle resourceBundle= ResourceBundle.getBundle(SCRETKEY_PATH);
		String scretKey = resourceBundle.getString(KEY);
		//비밀키를 Base64로 인코딩후 byte[]배열로 변환
		byte[] secret = Base64.getEncoder().encodeToString(scretKey.getBytes()).getBytes(StandardCharsets.UTF_8);
		scSecretKey=Keys.hmacShaKeyFor(secret);
		
	}
	
	
	/**
	 * JWT토큰을 생성해서 반환하는 메소드
	 * @param username 사용자 아이디	
	 * @param payloads 추가로 사용자 정보를 저장하기 위한 Claims
	 * @param expirationTime 토큰 만료 시간(15분에서 몇 시간이 적당).단위는 천분의 1초
	 * @return
	 */
	public static String createToken(String username,Map<String,Object> payloads,long expirationTime) {
		//JWT 토큰의 만료 시간 설정
		long currentTimeMillis = System.currentTimeMillis();//토큰의 생성시간		
		expirationTime = expirationTime+currentTimeMillis;//토큰의 만료시간	
		
		
		//Header 부분 설정
		Map<String,Object> headers= new HashMap<>();
		headers.put("typ", "JWT");
		headers.put("alg", "HS256");
		
		/*
		Claims객체 생성후 JwtBuilder의 claims(claims)로 해도 된다
		Claims claims = (Claims) Jwts.claims().setSubject(username);
		claims.putAll(payloads);
		claims.put("role", "ADMIN");*/
		
		JwtBuilder builder = Jwts.builder()
				.header().add(headers)// Headers 설정
				.and()
				.claims(payloads)// Claims 설정(기타 페이로드)
				.subject(username)//"sub"키로 사용자 ID 설정
				.issuedAt(new Date())//"iat"키로 생성 시간을 설정
				.expiration(new Date(expirationTime))//만료 시간 설정(필수로 설정하자.왜냐하면 토큰(문자열이라)은 세션처럼 제어가 안된다)
				.signWith(scSecretKey, Jwts.SIG.HS256);//비밀 키로 JWT를 서명
				
		//JWT 생성
		String jwt = builder.compact();
		return jwt;
	}///////
	/**
	 * 발급한 토큰의 payloads부분을 반환하는 메소드
	 * @param token  발급토큰	
	 * @return 토큰의 payloads부분 반환
	 */
	public static Map<String,Object> getTokenPayloads(String token){
		Map<String,Object> claims = new HashMap<>();
		
		try {
			//아래 코드 실행시 유효하지 않은 토큰이면 예외 발생한다
			claims = Jwts.parser()
					 .verifyWith(scSecretKey).build()//서명한 비밀키로 검증
					 .parseSignedClaims(token)//parseClaimsJws메소드는 만기일자 체크
					 .getPayload();
			return claims;
		}
		catch(Exception e) {
			claims.put("invalid", "유효하지 않는 토큰");
		}
		return claims;
	}/////////////////////////
	/**
	 * 유효한 토큰인지 검증하는 메소드
	 * @param token  발급토큰	
	 * @return 유효한 토큰이면 true,만료가 됬거나 변조된 토큰인 경우 false반환
	 */
	public static boolean verifyToken(String token) {
		try {
			//JWT토큰 파싱 및 검증
			Jws<Claims> claims= Jwts.parser().verifyWith(scSecretKey).build().parseSignedClaims(token);
			System.out.println("만기일자:"+claims.getPayload().getExpiration());
			return true;
		}
		catch(JwtException | IllegalArgumentException e) {
			System.out.println("유효하지 않은 토큰입니다:"+e.getMessage());
		}
		return false;
		
	}////////////////////////////////
	/**
	 * 
	 * @param request HttpServletRequest객체
	 * @param tokenName web.xml에 등록한 컨텍스트 초기화 파라미터 값(파라미터명는 "TOKEN-NAME")
	 * @return 발급받은 토큰 값
	 */
	
	public static String getTokenInCookie(HttpServletRequest request,String tokenName) {
		Cookie[] cookies = request.getCookies();	
		String token = "";
		if(cookies !=null){
			for(Cookie cookie:cookies){
				if(cookie.getName().equals(tokenName)){
					token = cookie.getValue();
				}
			}
		}
		return token;
	}///
}
