import { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
import eetch from 'eetch/eetch';

import Select from 'markup/pages/components/common/Select';
import Input from 'markup/pages/components/common/Input';

import * as s from 'markup/styles/components/intro/GroundInit';
const GroundInit = () => {
  const user = useSelector((state) => state.user);
  const [repositories, setRepositories] = useState([]);
  const [repository, setRepository] = useState(null);
  const [boards, setBoards] = useState([]);
  const [requests, setRequests] = useState([]);

  useEffect(() => {
    eetch
      .repoList({ Authorization: user.accessToken })
      .then((res) => {
        setRepositories(res);
      })
      .catch((err) => {
        console.log(err);
      });
  }, [user.accessToken]);

  useEffect(() => {
    console.log(repository);
  }, [repository]);

  return (
    <s.GroundWrapper>
      <s.Title>사용 가능한 그라운드가 없어요. (☍д⁰)</s.Title>
      <s.GroundDeck>
        <s.GroundCard>
          <Select label="리포지터리" list={repositories} select={setRepository} />
        </s.GroundCard>
        <s.GroundCard>
          <Input data="1231234234" label="그라운드 이름" />
          <Input data="5" label="집중 시간" />
          <Input data="5" label="연구 시간" />
          <Input label="문서 카테고리" array={boards} enter={setBoards} fixed={['일반', '공지사항']} />
          <Input label="요청 카테고리" array={requests} enter={setRequests} fixed={['오류', '개선']} />
        </s.GroundCard>
      </s.GroundDeck>
    </s.GroundWrapper>
  );
};

export default GroundInit;
