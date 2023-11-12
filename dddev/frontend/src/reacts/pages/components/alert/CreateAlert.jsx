// import eetch from 'eetch/eetch';
import { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
import Select from 'reacts/pages/components/common/SelectUser';
import Input from 'reacts/pages/components/common/Input';
import eetch from 'eetch/eetch';

const CreateAlert = () => {
  const user = useSelector((state) => state.user);
  const groundsMap = useSelector((state) => state.user.groundsMap);
  const [type, setType] = useState(null);
  const [ground, setGround] = useState(null);
  const [keywords, setKeywords] = useState([]);
  const [types, setTypes] = useState([{}]);

  useEffect(() => {}, [user.accessToken, user.refreshToken]);

  useEffect(() => {
    const list = [
      { id: 0, name: 'push' },
      { id: 1, name: 'pull_request' },
    ];
    setTypes(list);
    // console.log(keywords);

    // 알림 리스트 조회
    eetch
      .alertList({ accessToken: user.accessToken, refreshToken: user.refreshToken })
      .then()
      .catch((err) => {
        console.log(err);
      });
  }, [keywords]);

  // 알림 생성 api 호출
  const submitAlert = () => {
    // console.log(ground);
    eetch.createAlert({ accessToken: user.accessToken, refreshToken: user.refreshToken, repoId: 0, keyword: keywords, type }).catch((err) => {
      console.log(err);
    });
  };

  return (
    <div>
      <Select label='ground' list={groundsMap} data='ground' select={setGround} />
      <Select label='type' list={types} data='type' select={setType} />
      <Input label='키워드' array={keywords} enter={setKeywords} />
      <div type='button' onClick={submitAlert}>
        추가
      </div>
    </div>
  );
};

export default CreateAlert;
