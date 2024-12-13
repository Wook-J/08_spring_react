// axios API 설정 부분, https://axios-http.com/kr/docs/req_config 참고
import axios from 'axios';

export const axiosAPI = axios.create({
  baseURL : 'http://localhost:8080',  // 요청주소 앞부분에 자동 완성
  headers : {'Content-Type' : 'application/json'} /*,
  withCredentials : true */           // 쿠키 포함 설정(response에 쿠키 받아야 주고받을 수 있음)
  // 이거 할땐 서버에서도 credential 허용 설정 필요함
  // STS 내 @CrossOrigin 에서 allowCredentials= "true" 추가 해야함
});
