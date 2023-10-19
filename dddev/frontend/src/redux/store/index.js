import { createStore, combineReducers } from 'redux';
import menuReducer from 'redux/reducers/menu';
import groundReducer from 'redux/reducers/ground';

const rootReducer = combineReducers({
  menu: menuReducer,
  ground: groundReducer,
});

const store = createStore(rootReducer);

export default store;
