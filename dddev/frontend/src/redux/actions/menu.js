export const TOGGLE_MENU = 'TOGGLE_MENU';
export const SET_MENU = 'SET_MENU';
export const SET_MESSAGE = 'SET_MESSAGE';

export const toggleMenu = () => {
  return { type: TOGGLE_MENU };
};

export const setMenu = (value) => {
  return { type: SET_MENU, payload: value };
};

export const setMessage = (value) => {
  return { type: SET_MESSAGE, payload: value };
};
