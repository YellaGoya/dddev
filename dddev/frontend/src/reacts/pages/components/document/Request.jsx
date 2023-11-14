import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';

import eetch from 'eetch/eetch';
import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { logoutUser } from 'redux/actions/user';

import Write from 'reacts/pages/components/document/Write';
import SelectTransparent from 'reacts/pages/components/common/SelectTransparent';

const sortList = [
  { id: 0, name: '전체', filter: 'step2' },
  { id: 1, name: '해야 할 일', filter: 'todo' },
  { id: 2, name: '진행 중', filter: 'proceed' },
  { id: 3, name: '완료', filter: 'done' },
];

import * as s from 'reacts/styles/components/document/Request';
const Request = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const params = useParams();
  const user = useSelector((state) => state.user);
  const [requestList, setRequestList] = useState([]);
  const [refreshKey, setRefreshKey] = useState(0);
  const [filter, setFilter] = useState('step2');

  useEffect(() => {
    eetch
      .listRequest({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: params.groundId, filter: 'step2' })
      .then((res) => {
        setRequestList(res.data.reverse());
        navigate(`/${params.groundId}/document/request/${res.data[0].id}`);
      })
      .catch((err) => {
        if (err.message === 'RefreshTokenExpired') {
          dispatch(logoutUser());
          dispatch(setMenu(false));
          dispatch(setMessage(false));
          navigate(`/login`);
        }
      });
  }, []);

  useEffect(() => {
    eetch
      .listRequest({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: params.groundId, filter })
      .then((res) => {
        setRequestList(res.data.reverse());
        navigate(`/${params.groundId}/document/request/${res.data[0].id}`);
      })
      .catch((err) => {
        if (err.message === 'RefreshTokenExpired') {
          dispatch(logoutUser());
          dispatch(setMenu(false));
          dispatch(setMessage(false));
          navigate(`/login`);
        }
      });
  }, [filter]);

  useEffect(() => {
    setRefreshKey(params['*']);
  }, [params['*']]);

  return (
    <s.RequestWrapper>
      <s.RequestListWrapper>
        <SelectTransparent
          label="필터"
          list={sortList}
          selected={sortList[0].name}
          select={(item) => {
            setFilter(item.filter);
          }}
        />
        <s.RequestList>
          {requestList.map((item) => (
            <s.RequestItem key={item.id} onClick={() => navigate(`/${params.groundId}/document/request/${item.id}`)}>
              <s.ItemWrapper>
                <s.ItemAuthor>{item.author}</s.ItemAuthor>
                <s.ItemTitle>{item.title === '' ? '새 문서' : item.title}</s.ItemTitle>
              </s.ItemWrapper>
            </s.RequestItem>
          ))}
        </s.RequestList>
      </s.RequestListWrapper>

      <s.WriterWrapper>
        <Write key={refreshKey} />
      </s.WriterWrapper>
    </s.RequestWrapper>
  );
};

export default Request;
