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
  console.log('alertList res :: ', res);
  if (!res.ok) throw new Error('gayeonNotGood');

  return res;
};

export const createAlert = async ({ accessToken, refreshToken, alert }) => {
  const url = base + `/alert-service/create-alert`;
  const options = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
      'Authorization-refresh': refreshToken,
    },
    body: JSON.stringify({ alert }),
  };

  const res = await eetch(url, options);
  console.log('alertList res :: ', res);
  if (!res.ok) throw new Error('gayeonNotGood');

  return res;
};
