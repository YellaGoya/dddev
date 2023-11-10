export const SET_DOC = 'SET_DOC';

export const setDoc = (value) => {
  return {
    type: SET_DOC,
    payload: value,
  };
};
