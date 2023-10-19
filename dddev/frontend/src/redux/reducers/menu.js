import { SET_MENU } from '../actions/menu';

const initialState = {
  menuToggle: false,
};

const menuReducer = (state = initialState, action) => {
  switch (action.type) {
    case SET_MENU:
      return { ...state, menuToggle: !state.menuToggle };
    default:
      return state;
  }
};

export default menuReducer;
