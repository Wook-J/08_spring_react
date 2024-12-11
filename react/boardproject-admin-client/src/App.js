// import logo from './logo.svg';
import { useContext } from 'react';
import '../src/css/App.css';  // 더 짧게 쓰려면 './css/App.css'
import DashBoard from './components/DashBoard';
import Login from './components/Login';
import { AuthContext, AuthProvider } from './components/AuthContext';
import { BrowserRouter } from 'react-router';

// Context API 를 사용하는 방법 2가지

// 1. 컴포넌트 분리하여 하위 컴포넌트에서 useContext 이용하는 방법
// 이미 감싸져 있어서 소비만 하면 됨
function App () {
  return(     // 제공먼저하고 소비하기 위해서 사용하는 함수
    <AuthProvider>
      <AppComponent />
    </AuthProvider>
  )
}

function AppComponent() {

  // 로그인을 했다면 DashBoard 랜더링
  // 로그인을 안했다면 Login 랜더링
  // -> 조건 : 로그인 여부 -> 이를 기억해줄 상태값이 필요 (user로 설정)
  // -> user 상태에는 로그인한 사람에 대한 정보를 세팅
  // -> 전역 관리를 해야 함
  // -> user 라는 상태는 App 에서 뿐만아니라
  //    App의 자식(하위) 컴포넌트에서도 이용 가능해야함
  // -> Context API 사용 예정

  const { user } = useContext(AuthContext);
  // user 만 필요해서 AuthContext 중 {user}를 통해 가져옴 (알아서 consume 하는 것임)
  // 요즘은 Hook 을 많이 이용하는 추세여서 이 방법이 현재 트랜드임

  return (
    <>
      {user ?
      (
        <div className='body-container'>
          <BrowserRouter>
            <DashBoard />
          </BrowserRouter>
        </div>
      ) : (
        <div className='login-section'>
          <Login />
        </div>
      )

      }
    </>
  );
}


// 2. <AuthProvider> 안에서 <AuthContext.Consumer> 이용하는 방법
// Hook 이용 안하고 Context API 기본형태로 이용
// -> <AuthContext.Consumer> 안에서 익명함수 형태로 전역 상태를 꺼내어 사용

// function App() {
//   return (
//     <AuthProvider>
//       <AuthContext.Consumer>
//         {({ user }) =>
//           user ? (
//             <div className="body-container">
//               <DashBoard />
//             </div>
//           ) : (
//             <div className="login-section">
//               <Login />
//             </div>
//           )
//         }
//       </AuthContext.Consumer>
//     </AuthProvider>
//   );
// }

export default App;
