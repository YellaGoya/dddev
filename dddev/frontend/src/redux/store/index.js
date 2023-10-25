import { createStore, combineReducers } from 'redux';
import { persistStore, persistReducer } from 'redux-persist';
import storage from 'redux-persist/lib/storage';

import menuReducer from 'redux/reducers/menu';
import groundReducer from 'redux/reducers/ground';
import userReducer from 'redux/reducers/user';

const rootReducer = combineReducers({
  menu: menuReducer,
  ground: groundReducer,
  user: userReducer,
});

const persistConfig = {
  key: 'root',
  storage,
};

const persistedReducer = persistReducer(persistConfig, rootReducer);

export const store = createStore(persistedReducer);
export const persistor = persistStore(store);
