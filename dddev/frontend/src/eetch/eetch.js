import * as user from 'eetch/user';
// import * as notice from "apis/notice";
// import * as question from "apis/question";
// import * as statistics from "apis/statistics";
// import * as test from "apis/test";
// import * as user from "apis/user";
// import * as history from "apis/history";
// import * as practice from "apis/practice";

// import { refresh } from "apis/user";
// import { updateRefresh } from "redux/slice/userSlice";

const eetch = async (url, options) => {
  const response = await fetch(url, options);

  return response.json();
};

eetch.githubSync = (values) => user.githubSync(values);

// eetch.valid = (values) => user.valid(values);
// eetch.tokenValidation = async (call, values, dispatch) => {
//     return call(values).catch(() => {
//         if (values.user.refreshToken) {
//             return refresh(values.user)
//                 .then((res) => {
//                     dispatch(
//                         updateRefresh({
//                             accessToken: res.accessToken
//                         })
//                     );

//                     const newValues = {
//                         ...values,
//                         user: {
//                             accessToken: res.accessToken,
//                             refreshToken: values.user.refreshToken
//                         }
//                     };

//                     return call(newValues);
//                 })
//                 .catch(() => {
//                     throw new Error("토큰이 만료되었습니다. 다시 로그인해주세요.");
//                 });
//         }

//         throw new Error("토큰이 없습니다. 다시 로그인해주세요.");
//     });
// };

// eetch.valid = (values) => user.valid(values);
// eetch.refresh = (values) => user.refresh(values);
// eetch.signin = (values) => user.signin(values);
// eetch.signup = (values) => user.signup(values);
// eetch.signout = (values) => user.signout(values);
// eetch.sendCode = (values) => user.sendCode(values);
// eetch.unregist = (values) => user.unregist(values);
// eetch.updatePw = (values) => user.updatePw(values);
// eetch.resetPw = (values) => user.resetPw(values);
// eetch.updateProfile = (values) => user.updateProfile(values);
// eetch.mypage = (values) => user.mypage(values);
// eetch.checkEmail = (values) => user.checkEmail(values);
// eetch.checkCode = (values) => user.checkCode(values);
// eetch.checkNickname = (values) => user.checkNickname(values);
// eetch.reset = (values) => user.reset(values);
// eetch.domain = (values) => user.domain(values);

// eetch.continuous = (values) => history.continuous(values);

export default eetch;
