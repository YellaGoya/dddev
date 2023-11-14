import eetch from 'eetch/eetch';

export const githubSync = async ({ code }) => {
  const url = `https://k9d103.p.ssafy.io:8001/oauth/sign-in?code=${code}`;
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const res = await eetch(url, options);

  const accessToken = res.headers.get('Authorization');
  const message = await res.text();

  const base64Url = accessToken.split('.')[1];
  const base64 = base64Url.replace('-', '+').replace('_', '/');
  const payload = JSON.parse(window.atob(base64));

  const expirationDate = new Date(payload.exp * 1000);

  return {
    accessToken,
    accessExp: expirationDate,
    refreshToken: res.headers.get('Authorization-Refresh'),
    nickname: res.headers.get('nickname'),
    role: res.headers.get('role'),
    lastGround: res.headers.get('Lastgroundid'),
    message,
    githubId: res.headers.get('githubId'),
  };
};

export const githubTokenRegist = async ({ accessToken, refreshToken, personalAccessToken }) => {
  const url = `https://k9d103.p.ssafy.io:8001/user/personal-access-token`;
  const options = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
    },
    body: JSON.stringify({ personalAccessToken }),
  };

  const res = await eetch(url, options, refreshToken);

  return res;
};

export const userEdit = async ({ accessToken, refreshToken, nickname, statusMsg }) => {
  const url = `https://k9d103.p.ssafy.io:8001/user`;
  const options = {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
    },
    body: JSON.stringify({ nickname, statusMsg }),
  };

  const res = await eetch(url, options, refreshToken);

  return res.json();
};

export const userInfo = async ({ accessToken, refreshToken }) => {
  const url = `https://k9d103.p.ssafy.io:8001/user`;
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
    },
  };

  const res = await eetch(url, options, refreshToken);

  return res.json();
};

export const userById = async ({ accessToken, refreshToken, githubId }) => {
  const url = `https://k9d103.p.ssafy.io:8001/user/${githubId}`;
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
    },
  };

  const res = await eetch(url, options, refreshToken);
  return res.json();
};

export const userNickname = async ({ accessToken, refreshToken, nickname }) => {
  const url = `https://k9d103.p.ssafy.io:8001/user/nickname/duplicate/${nickname}`;
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
    },
  };

  const res = await eetch(url, options, refreshToken);

  return res.json();
};

export const userGrounds = async ({ accessToken, refreshToken }) => {
  const url = `https://k9d103.p.ssafy.io:8001/user/ground/list`;
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
    },
  };

  const res = await eetch(url, options, refreshToken);

  return res.json();
};

export const changeLastGround = async ({ accessToken, refreshToken, groundId }) => {
  const url = `https://k9d103.p.ssafy.io:8001/user/last-ground/${groundId}`;
  const options = {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
    },
  };

  const res = await eetch(url, options, refreshToken);

  return res.json();
};

export const userProfileImage = async ({ accessToken, refreshToken }) => {
  const url = `https://k9d103.p.ssafy.io:8001/user/profile`;
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
    },
  };

  const res = await eetch(url, options, refreshToken);

  return res;
};

export const userUploadImage = async ({ accessToken, refreshToken, formData }) => {
  const url = `https://k9d103.p.ssafy.io:8001/user/profile`;
  const options = {
    method: 'PUT',
    headers: {
      Authorization: accessToken,
    },
    body: formData,
  };

  const res = await eetch(url, options, refreshToken);

  return res;
};

export const userDeleteImage = async ({ accessToken, refreshToken }) => {
  const url = `https://k9d103.p.ssafy.io:8001/user/profile`;
  const options = {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
    },
  };

  const res = await eetch(url, options, refreshToken);

  return res;
};
