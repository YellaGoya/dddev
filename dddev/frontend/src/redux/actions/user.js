export const LOGIN_USER = 'LOGIN_USER';
export const LOGOUT_USER = 'LOGOUT_USER';
export const UPDATE_USER = 'UPDATE_USER';

export const loginUser = ({ accessToken, accessExp, refreshToken, lastGround, groundsList, groundsMap, groundsMine, githubId }) => {
  return {
    type: LOGIN_USER,
    payload: {
      accessToken,
      accessExp,
      refreshToken,
      lastGround,
      groundsList,
      groundsMap,
      groundsMine,
      githubId,
    },
  };
};

export const logoutUser = () => {
  return {
    type: LOGOUT_USER,
  };
};

export const updateUser = ({ accessToken, accessExp, refreshToken, lastGround, groundsList, groundsMap, groundsMine, githubId, unread }) => {
  console.log('here', unread);
  return {
    type: UPDATE_USER,
    payload: {
      accessToken,
      accessExp,
      refreshToken,
      lastGround,
      groundsList,
      groundsMap,
      groundsMine,
      githubId,
      unread,
    },
  };
};
