import React, { useContext } from 'react';
import '../css/DashBoard.css';
import Restore from './Restore.js';
import Manager from './Manager.js';
import Statistics from './Statistics.js';
import { AuthContext } from './AuthContext.js';
import { NavLink, Route, Routes } from 'react-router';

// react-router-dom 을 이용한 라우팅 방법
// react-router-dom : React 어플리케이션에서 라우팅을 구현하기 위해 사용하는 라이브러리
// 라우팅(routing) : 사용자가 요청한 URL 경로를 따라 적절한 페이지 or 리소스를 제공하는 과정
export default function DashBoard() {

  const globalState = useContext(AuthContext);

  return (
      <div className='dash-board-container'>
        <h1>관리자 페이지</h1>

        <div className='admin-info'>
          <p>현재 접속 관리자 : {globalState.user.memberNickname}</p>
          <button onClick={globalState.handleLogout}>로그아웃</button>
        </div>

        <div className='router-tab-box'>
          <NavLink to="/restore">복구</NavLink>
          <NavLink to="/statistics">통계</NavLink>
          <NavLink to="/manager">관리자 메뉴</NavLink>
        </div>

        {/* Route 를 이용하여 각 컴포넌트 연결 */}
        <Routes>
          <Route path='/' element={<h1>DasbBoard 메인</h1>} />
          <Route path='/restore' element={<Restore/>} />
          <Route path='/statistics' element={<Statistics/>} />
          <Route path='/manager' element={<Manager />} />
        </Routes>

      </div>
  )
}


// 상태값에 따라 렌더링할 컴포넌트를 변경하는 방법(선생님 깃허브 참고)
// 전부다 최상위 경로로만 표시되어 있음(가장 첫번째 상태를 기억하고 있음)