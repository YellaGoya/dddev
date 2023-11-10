export const LOGIN_USER = 'LOGIN_USER';
export const LOGOUT_USER = 'LOGOUT_USER';
export const UPDATE_USER = 'UPDATE_USER';

export const loginUser = ({ accessToken, accessExp, refreshToken, lastGround, groundsList, groundsMap, groundsMine }) => {
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
    },
  };
};

export const logoutUser = () => {
  return {
    type: LOGOUT_USER,
  };
};

export const updateUser = ({ accessToken, accessExp, refreshToken, lastGround, groundsList, groundsMap, groundsMine }) => {
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
    },
  };
};
