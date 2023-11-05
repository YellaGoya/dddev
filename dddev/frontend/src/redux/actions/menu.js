export const SET_MENU = 'SET_MENU';
export const SET_MESSAGE = 'SET_MESSAGE';

export const setMenu = () => {
  return { type: SET_MENU };
};

export const setMessage = (value) => {
  return { type: SET_MESSAGE, payload: value };
};
