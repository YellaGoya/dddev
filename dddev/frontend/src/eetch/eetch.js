import * as user from 'eetch/user';
import * as repo from 'eetch/repo';
import * as ground from 'eetch/ground';
import * as test from 'eetch/test';
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
  const res = await fetch(url, options);
  if (res.status === 403) {
    const newAccessToken = await fetch(`https://k9d103.p.ssafy.io/oauth/re-issue`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization-refresh': options.headers['Authorization-refresh'],
      },
    });

    if (!newAccessToken.ok) throw new Error(`RefreshTokenExpired`);

    options.accessToken = newAccessToken;
    const retry = await fetch(url, options);
    if (!retry.ok) throw new Error(`${retry.status} 에러`);

    return retry;
  }

  if (!res.ok && res.status !== 403) throw new Error(`${res.status} 에러`);

  return res;
};

eetch.addDeviceToken = (values) => test.addDeviceToken(values);
eetch.alertList = (values) => test.alertList(values);

eetch.githubSync = (values) => user.githubSync(values);
eetch.githubTokenRegist = (values) => user.githubTokenRegist(values);
eetch.githubTokenRegist = (values) => user.githubTokenRegist(values);
eetch.userEdit = (values) => user.userEdit(values);
eetch.userInfo = (values) => user.userInfo(values);
eetch.userGrounds = (values) => user.userGrounds(values);
eetch.changeLastGround = (values) => user.changeLastGround(values);
eetch.userProfileImage = (values) => user.userProfileImage(values);
eetch.userUploadImage = (values) => user.userUploadImage(values);
eetch.userDeleteImage = (values) => user.userDeleteImage(values);

eetch.repoList = (values) => repo.repoList(values);

eetch.groundCreate = (values) => ground.groundCreate(values);
eetch.groundInfo = (values) => ground.groundInfo(values);

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
