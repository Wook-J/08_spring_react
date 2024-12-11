import axios from "axios";
import { createContext, useState } from "react";

export const AuthContext = createContext();
// Context 는 React 에서 컴포넌트 계층 구조(트리)를 통해 데이터를 효율적으로 전달하기 위한 메커니즘
// -> 컴포넌트 간 전역 상태를 공유할 수 있는 컨텍스트를 생성

// Context 는 Provider(제공자) 와 Consumer(소비자)가 있음

// 전역상태 Provider(제공자) 정의
export const AuthProvider = ({ children }) => {

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [user, setUser] = useState(() => {
    const storeUser = localStorage.getItem('userData');
    return storeUser ? JSON.parse(storeUser) : null;
  });

  // 이벤트 핸들러 종류
  // 1. 이메일 입력 핸들러
  const changeInputEmail = (e) => {
    setEmail(e.target.value);     // e.target : Login.js 내 input태그(이메일)
  }

  // 2. 비밀번호 입력 핸들러
  const changeInputPw = (e) => {
    setPassword(e.target.value);  // e.target : Login.js 내 input태그(비밀번호)
  }

  // 3. 로그인 처리 함수 (비동기요청, async await 사용)
  const handleLogin = async (e) => {
    e.preventDefault();

    // 비동기로 로그인 요청
    // console.log(email);
    // console.log(password);

    try {
      const response = await axios.post(
        'http://localhost:8080/admin/login',
        {memberEmail: email, memberPw: password},
        {'Content-Type': 'application/json'}
      );

      console.log(response);    // 확인용
      
      const adminInfo = response.data;

      if(adminInfo.length === 0){
        alert('이메일 혹은 비밀번호 불일치');
        return;
      }

      // 브라우저에서 현재 로그인한 관리자 정보 기억하기
      /**
       * localStorage
       * - 브라우저를 닫아도 데이터가 영구적으로 유지
       * - 브라우저 전역에서 사용 (모든 탭과 창에서 공유됨)
       * 
       * sessionStorage
       * - 브라우저 탭 또는 창을 닫으면 데이터가 즉시 삭제
       * - 현재 탭 또는 창에만 데이터가 유지됨
       */

      const currentTime = new Date().getTime();
      const expirationTime = currentTime + 60 * 60 * 1000;  // 1시간 (ms 단위)
      // 데이터와 만료시간을 localStorage 저장
      // * localStorage 는 만료시간을 지정하는 기능 내장 X (필요 시 따로 직접 구현)
      localStorage.setItem('userData', JSON.stringify(adminInfo));
      localStorage.setItem('expirationTime', expirationTime);

      // 상태에 세팅
      setUser(adminInfo);
      // 변화가 일어나서 리랜더링됨!! app.js에서부터 변화된 부분이 적용됨!

      // 만료시간을 확인하기 위한 타이머 설정
      setTimeout(() => {
        // 만료시간이 지나면 localStorage에 있는 데이터 삭제
        localStorage.removeItem('userData');
        localStorage.removeItem('expirationTime');
        alert('재로그인 해주세요');
        window.location.href = '/';
      }, 60 * 60 * 1000);   // 1시간 후 (ms 단위)

    } catch(err) {
      console.log('로그인 중 예외 발생 : ', err);
    }
  }

  // 4. 로그아웃 처리 함수
  const handleLogout = async (e) => {
    try {
      // get 비동기 요청, 요청주소(/admin/logout), get요청은 뒤에 본문 헤더 없어도 됨
      const response = await axios.get('http://localhost:8080/admin/logout');

      // console.log(response);  // 데이터 확인용

      if(response.status === 200){
        setUser(null);                                  // 1. setUser(null);
        localStorage.removeItem('userData');            // 2. localStorage 비우기 1
        localStorage.removeItem('expirationTime');      // 2. localStorage 비우기 2
        alert('로그아웃 되었습니다.');
      }

    } catch (err) {
      console.log('로그아웃 중 예외 발생 : ', err);
    }

    
    
  }

  // user 라는 상태와 여러가지 이벤트 핸들러(함수)를 묶어서
  // Provider 를 통해 하위(자식) 컴포넌트로 데이터를 전달
  const globalState = {
    user, 
    changeInputEmail,
    changeInputPw,
    handleLogin,
    handleLogout
  };

  return (
    // AuthContext.Provider : 데이터를 제공하는 역할
    // 하위 컴포넌트는 이 Provider 가 제공하는 데이터를 사용(== Consumer)할 수 있음
    <AuthContext.Provider value={globalState}>
      {children}
    </AuthContext.Provider>
  )
}