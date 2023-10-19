import { SET_GROUND } from '../actions/ground';

const initialState = {
  ground: '',
};

const groundReducer = (state = initialState, action) => {
  switch (action.type) {
    case SET_GROUND:
      return { ...state, ground: action.payload };
    default:
      return state;
  }
};

export default groundReducer;
