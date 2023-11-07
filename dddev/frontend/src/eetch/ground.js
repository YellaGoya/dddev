import eetch from 'eetch/eetch';

export const createGround = async ({ accessToken, refreshToken, name, repoId }) => {
  const url = `https://k9d103.p.ssafy.io/ground/repo/${repoId}`;
  const options = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
    },
    body: JSON.stringify({ name }),
  };

  const res = await eetch(url, options, refreshToken);

  return res.json();
};

export const getGround = async ({ accessToken, refreshToken, groundId }) => {
  const url = `https://k9d103.p.ssafy.io/ground/${groundId}`;
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

export const editGround = async ({ accessToken, refreshToken, groundId, name, focusTime, activeTime }) => {
  const url = `https://k9d103.p.ssafy.io/ground/${groundId}`;
  const options = {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
    },
    body: JSON.stringify({ name, focusTime, activeTime }),
  };

  const res = await eetch(url, options, refreshToken);

  return res.json();
};

export const createDocument = async ({ accessToken, refreshToken, groundId, type, parentId, sprintId }) => {
  const url = `https://k9d103.p.ssafy.io/ground/${groundId}/${type}/create`;
  const options = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
    },
    body: JSON.stringify({ parentId, sprintId }),
  };

  const res = await eetch(url, options, refreshToken);

  return res.json();
};

export const listDocument = async ({ accessToken, refreshToken, groundId, type, parentId }) => {
  const url = `https://k9d103.p.ssafy.io/ground/${groundId}/${type}/${type === 'target' ? '' : parentId + '/'}list`;
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

export const treeDocument = async ({ accessToken, refreshToken, type, groundId }) => {
  const url = `https://k9d103.p.ssafy.io/ground/${groundId}/${type}/total`;
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

export const detailDocument = async ({ accessToken, refreshToken, groundId, type, id }) => {
  const url = `https://k9d103.p.ssafy.io/ground/${groundId}/${type}/${id}`;
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

export const editDocument = async ({ accessToken, refreshToken, groundId, type, id, title, content }) => {
  const url = `https://k9d103.p.ssafy.io/ground/${groundId}/${type}/${id}`;
  const options = {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
    },
    body: JSON.stringify({ title, content }),
  };

  const res = await eetch(url, options, refreshToken);

  return res.json();
};

export const deleteDocument = async ({ accessToken, refreshToken, groundId, type, id }) => {
  const url = `https://k9d103.p.ssafy.io/ground/${groundId}/${type}/${id}`;
  const options = {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
    },
  };

  const res = await eetch(url, options, refreshToken);

  return res.json();
};
