import eetch from 'eetch/eetch';

const base = `http://localhost:8089`;

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
  // console.log(res.message);
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
  // console.log('alertList res :: ', res);
  if (!res.ok) throw new Error('gayeonNotGood');

  return res;
};

export const createAlert = async ({ accessToken, refreshToken, repoId, keyword, type }) => {
  const url = base + `/alert-service/create-alert`;
  const options = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
      'Authorization-refresh': refreshToken,
    },
    body: JSON.stringify({ repoId, keyword, type }),
  };

  const res = await eetch(url, options);
  // console.log('alertList res :: ', res);
  if (!res.ok) throw new Error('gayeonNotGood');

  return res;
};

export const updateAlert = async ({ accessToken, refreshToken, alertId, keyword, type }) => {
  const url = base + `/alert-service/${alertId}`;
  const options = {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
      'Authorization-refresh': refreshToken,
    },
    body: JSON.stringify({ keyword, type }),
  };

  const res = await eetch(url, options);
  // console.log('alertList res :: ', res);
  if (!res.ok) throw new Error('gayeonNotGood');

  return res;
};

export const deleteAlert = async ({ accessToken, refreshToken, alertId }) => {
  const url = base + `/alert-service/${alertId}`;
  const options = {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
      'Authorization-refresh': refreshToken,
    },
  };

  const res = await eetch(url, options);
  // console.log('alertList res :: ', res);
  if (!res.ok) throw new Error('gayeonNotGood');

  return res;
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
