import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';

import eetch from 'eetch/eetch';
import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { logoutUser } from 'redux/actions/user';

import Write from 'reacts/pages/components/document/Write';
import SelectTransparent from 'reacts/pages/components/common/SelectTransparent';
import userStockImage from 'assets/userStockImage.webp';

const sortList = [
  { id: 0, name: '전체', filter: 'step2' },
  { id: 1, name: '해야 할 일', filter: 'todo' },
  { id: 2, name: '진행 중', filter: 'proceed' },
  { id: 3, name: '완료', filter: 'done' },
];

import FolderCopyRoundedIcon from '@mui/icons-material/FolderCopyRounded';

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
      {requestList && requestList.length > 0 ? (
        <>
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
                    <s.ItemAuthor>
                      <s.ProfileImage src={item.author.fileName ? `https://k9d103.p.ssafy.io/img/user/${item.author.fileName}` : userStockImage} />
                      {item.author.nickname}
                    </s.ItemAuthor>
                    <s.ItemTitle>{item.title === '' ? '새 문서' : item.title}</s.ItemTitle>
                  </s.ItemWrapper>
                </s.RequestItem>
              ))}
            </s.RequestList>
          </s.RequestListWrapper>

          <s.WriterWrapper>
            <Write key={refreshKey} />
          </s.WriterWrapper>
        </>
      ) : (
        <s.ThereIsNothing>
          <h1>생성 된 요청이 없습니다.</h1>
          <s.ToDocsButton>
            문서 페이지로 이동
            <FolderCopyRoundedIcon />
          </s.ToDocsButton>
        </s.ThereIsNothing>
      )}
    </s.RequestWrapper>
  );
};

export default Request;
