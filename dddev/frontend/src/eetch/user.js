export const githubSync = async ({ code }) => {
  const url = `https://k9d103.p.ssafy.io/oauth/sign-in?code=${code}`;
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const res = await fetch(url, options);
  if (!res.ok) throw new Error(`${res.status} 에러`);

  const accessToken = res.headers.get('Authorization');
  const message = await res.text();

  const base64Url = accessToken.split('.')[1];
  const base64 = base64Url.replace('-', '+').replace('_', '/');
  const payload = JSON.parse(window.atob(base64));

  const expirationDate = new Date(payload.exp * 1000);

  console.log(expirationDate);

  return {
    accessToken,
    accessExp: expirationDate,
    refreshToken: res.headers.get('Authorization-Refresh'),
    nickname: res.headers.get('nickname'),
    role: res.headers.get('role'),
    lastGround: res.headers.get('Lastgroundid'),
    message,
  };
};

export const githubTokenRegist = async ({ Authorization, personalAccessToken }) => {
  const url = `https://k9d103.p.ssafy.io/user/personal-access-token`;
  const options = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization,
    },
    body: JSON.stringify({ personalAccessToken }),
  };

  const res = await fetch(url, options);
  if (!res.ok) throw new Error(`${res.status} 에러`);

  return res;
};

export const userEdit = async ({ Authorization, nickname, statusMsg }) => {
  const url = `https://k9d103.p.ssafy.io/user`;
  const options = {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      Authorization,
    },
    body: JSON.stringify({ nickname, statusMsg }),
  };

  const res = await fetch(url, options);
  if (!res.ok) throw new Error(`${res.status} 에러`);

  return res.json();
};

export const userInfo = async ({ Authorization }) => {
  const url = `https://k9d103.p.ssafy.io/user`;
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization,
    },
  };

  const res = await fetch(url, options);
  if (!res.ok) throw new Error(`${res.status} 에러`);

  return res.json();
};

export const userGrounds = async ({ Authorization }) => {
  const url = `https://k9d103.p.ssafy.io/user/ground/list`;
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization,
    },
  };

  const res = await fetch(url, options);
  if (!res.ok) throw new Error(`${res.status} 에러`);

  return res.json();
};

export const userProfileImage = async ({ Authorization }) => {
  const url = `https://k9d103.p.ssafy.io/user/profile`;
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization,
    },
  };

  const res = await fetch(url, options);
  if (!res.ok) throw new Error(`${res.status} 에러`);

  return res;
};

export const userUploadImage = async ({ Authorization, formData }) => {
  console.log(formData.get('file'));
  const url = `https://k9d103.p.ssafy.io/user/profile`;
  const options = {
    method: 'PUT',
    headers: {
      Authorization,
    },
    body: formData,
  };

  const res = await fetch(url, options);
  if (!res.ok) throw new Error(`${res.status} 에러`);

  return res;
};

export const userDeleteImage = async ({ Authorization }) => {
  const url = `https://k9d103.p.ssafy.io/user/profile`;
  const options = {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
      Authorization,
    },
  };

  const res = await fetch(url, options);
  if (!res.ok) throw new Error(`${res.status} 에러`);

  return res;
};
