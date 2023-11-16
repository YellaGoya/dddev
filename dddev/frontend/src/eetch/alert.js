import eetch from 'eetch/eetch';

const base = `https://k9d103.p.ssafy.io:8001`;
// const base = `http://localhost:8000`;

export const addDeviceToken = async ({ accessToken, refreshToken, deviceToken }) => {
  const url = base + `/user/device-token`;
  const options = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
      'Authorization-refresh': refreshToken,
    },
    body: JSON.stringify({ deviceToken }),
  };

  const res = await eetch(url, options);
  if (!res.ok) throw new Error('gayeonNotGood');

  return res;
};

export const alertList = async ({ accessToken, refreshToken }) => {
  const url = base + `/alert-service`;
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
      'Authorization-refresh': refreshToken,
    },
  };

  const res = await eetch(url, options);
  if (!res.ok) throw new Error('gayeonNotGood');

  return res;
};

export const getAlert = async ({ accessToken, refreshToken, groundId }) => {
  const url = base + `/alert-service/${groundId}`;
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
      'Authorization-refresh': refreshToken,
    },
  };

  const res = await eetch(url, options);
  if (!res.ok) throw new Error('gayeonNotGood');

  return res.json();
};

export const createAlert = async ({ accessToken, refreshToken, groundId, keyword, type }) => {
  const url = base + `/alert-service/create-alert`;
  const options = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
      'Authorization-refresh': refreshToken,
    },
    body: JSON.stringify({ groundId, keyword, type }),
  };

  const res = await eetch(url, options);
  if (!res.ok) throw new Error('gayeonNotGood');

  return res.json();
};

export const updateAlert = async ({ accessToken, refreshToken, groundId, keyword }) => {
  const url = base + `/alert-service/${groundId}`;
  const options = {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
      'Authorization-refresh': refreshToken,
    },
    body: JSON.stringify({ keyword }),
  };

  const res = await eetch(url, options);
  // if (!res.ok) throw new Error('gayeonNotGood');

  return res.json();
};

export const deleteAlert = async ({ accessToken, refreshToken, groundId, alertId }) => {
  const url = base + `/alert-service/${groundId}/${alertId}`;
  const options = {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
      'Authorization-refresh': refreshToken,
    },
  };

  const res = await eetch(url, options);
  if (!res.ok) throw new Error('gayeonNotGood');

  return res.json();
};

export const userGroundsExample = async ({ accessToken, refreshToken }) => {
  const url = base + `/user/ground/list`;
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

export const groundUsersExample = async ({ accessToken, refreshToken, groundId }) => {
  const url = base + `/ground/${groundId}/users`;
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
