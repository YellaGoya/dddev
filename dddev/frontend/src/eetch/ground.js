import eetch from 'eetch/eetch';

export const createGround = async ({ accessToken, refreshToken, name, repoId }) => {
  const url = `https://k9d103.p.ssafy.io:8001/ground/repo/${repoId}`;
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
  const url = `https://k9d103.p.ssafy.io:8001/ground/${groundId}`;
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
  const url = `https://k9d103.p.ssafy.io:8001/ground/${groundId}`;
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
  const url = `https://k9d103.p.ssafy.io:8001/ground/${groundId}/${type}/create`;
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
  const url = `https://k9d103.p.ssafy.io:8001/ground/${groundId}/${type}/${type === 'target' ? '' : parentId + '/'}list`;
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

export const linkDocument = async ({ accessToken, refreshToken, groundId, type, parentId, id }) => {
  const url = `https://k9d103.p.ssafy.io:8001/ground/${groundId}/${type}/${id}/connect`;
  const options = {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
    },
    body: JSON.stringify({ parentId }),
  };

  const res = await eetch(url, options, refreshToken);

  return res.json();
};

export const treeDocument = async ({ accessToken, refreshToken, type, groundId }) => {
  const url = `https://k9d103.p.ssafy.io:8001/ground/${groundId}/${type}/total`;
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

export const parentsList = async ({ accessToken, refreshToken, type, groundId }) => {
  const url = `https://k9d103.p.ssafy.io:8001/ground/${groundId}/${type}/list`;
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
  const url = `https://k9d103.p.ssafy.io:8001/ground/${groundId}/${type}/${id}`;
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
  const url = `https://k9d103.p.ssafy.io:8001/ground/${groundId}/${type}/${id}`;
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

export const statusDocument = async ({ accessToken, refreshToken, groundId, type, id, status }) => {
  const url = `https://k9d103.p.ssafy.io:8001/ground/${groundId}/${type}/${id}/status`;
  const options = {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
    },
    body: JSON.stringify({ status }),
  };

  const res = await eetch(url, options, refreshToken);

  return res.json();
};

export const senderDocument = async ({ accessToken, refreshToken, groundId, id, sender }) => {
  const url = `https://k9d103.p.ssafy.io:8001/ground/${groundId}/request/${id}/sender`;
  const options = {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
    },
    body: JSON.stringify({ sendUserId: sender }),
  };

  const res = await eetch(url, options, refreshToken);

  return res.json();
};

export const receiverDocument = async ({ accessToken, refreshToken, groundId, id, receiver }) => {
  const url = `https://k9d103.p.ssafy.io:8001/ground/${groundId}/request/${id}/receiver`;
  const options = {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
    },
    body: JSON.stringify({ receiveUserId: receiver }),
  };

  const res = await eetch(url, options, refreshToken);

  return res.json();
};

export const commentDocument = async ({ accessToken, refreshToken, groundId, id, comment }) => {
  const url = `https://k9d103.p.ssafy.io:8001/ground/${groundId}/request/${id}/comment`;
  const options = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
    },
    body: JSON.stringify({ comment }),
  };

  const res = await eetch(url, options, refreshToken);

  return res.json();
};

export const timeDocument = async ({ accessToken, refreshToken, groundId, type, id, focusTime, activeTime }) => {
  const url = `https://k9d103.p.ssafy.io:8001/ground/${groundId}/${type}/${id}/time`;
  const options = {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
    },
    body: JSON.stringify({ focusTime, activeTime }),
  };

  const res = await eetch(url, options, refreshToken);

  return res.json();
};

export const titleDocument = async ({ accessToken, refreshToken, groundId, type, id, title }) => {
  const url = `https://k9d103.p.ssafy.io:8001/ground/${groundId}/${type}/${id}/title`;
  const options = {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
    },
    body: JSON.stringify({ title }),
  };

  const res = await eetch(url, options, refreshToken);

  return res.json();
};

export const deleteDocument = async ({ accessToken, refreshToken, groundId, type, id }) => {
  const url = `https://k9d103.p.ssafy.io:8001/ground/${groundId}/${type}/${id}`;
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

export const groundUsers = async ({ accessToken, refreshToken, groundId }) => {
  const url = `https://k9d103.p.ssafy.io:8001/ground/${groundId}/users`;
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

export const groundUser = async ({ accessToken, refreshToken, groundId, email }) => {
  const url = `https://k9d103.p.ssafy.io:8001/ground/${groundId}/user/${email}`;
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

export const groundOwn = async ({ accessToken, refreshToken, groundId }) => {
  const url = `https://k9d103.p.ssafy.io:8001/ground/${groundId}/is-owner`;
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

export const groundInvite = async ({ accessToken, refreshToken, groundId, githubId }) => {
  const url = `https://k9d103.p.ssafy.io:8001/ground/${groundId}/invite/${githubId}`;
  const options = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
    },
  };

  const res = await eetch(url, options, refreshToken);

  return res.json();
};

export const groundOut = async ({ accessToken, refreshToken, groundId, githubId }) => {
  const url = `https://k9d103.p.ssafy.io:8001/ground/${groundId}/owner/user/${githubId}`;
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

export const createSprint = async ({ accessToken, refreshToken, groundId }) => {
  const url = `https://k9d103.p.ssafy.io:8001/ground/${groundId}/sprint`;
  const options = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: accessToken,
    },
  };

  const res = await eetch(url, options, refreshToken);

  return res.json();
};

export const listSprint = async ({ accessToken, refreshToken, groundId }) => {
  const url = `https://k9d103.p.ssafy.io:8001/ground/${groundId}/sprint`;
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

export const listRequest = async ({ accessToken, refreshToken, groundId, filter }) => {
  const url = `https://k9d103.p.ssafy.io:8001/ground/${groundId}/request/${filter}`;
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

export const listSprints = async ({ accessToken, refreshToken, groundId, sprintId }) => {
  const url = `https://k9d103.p.ssafy.io:8001/ground/${groundId}/issue/${sprintId}/sprint`;
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
