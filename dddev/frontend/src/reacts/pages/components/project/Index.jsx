import { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';

import eetch from 'eetch/eetch';

import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { logoutUser } from 'redux/actions/user';

import Modal from 'reacts/pages/components/common/Modal';

import DriveFileRenameOutlineRoundedIcon from '@mui/icons-material/DriveFileRenameOutlineRounded';
import RemoveCircleRoundedIcon from '@mui/icons-material/RemoveCircleRounded';
import BlindsClosedRoundedIcon from '@mui/icons-material/BlindsClosedRounded';
import FlagRoundedIcon from '@mui/icons-material/FlagRounded';
import CheckBoxRoundedIcon from '@mui/icons-material/CheckBoxRounded';
import ArrowCircleUpRoundedIcon from '@mui/icons-material/ArrowCircleUpRounded';
import ArrowCircleDownRoundedIcon from '@mui/icons-material/ArrowCircleDownRounded';
import HighlightOffRoundedIcon from '@mui/icons-material/HighlightOffRounded';
import RemoveCircleOutlineRoundedIcon from '@mui/icons-material/RemoveCircleOutlineRounded';
import AddCircleOutlineRoundedIcon from '@mui/icons-material/AddCircleOutlineRounded';
import DirectionsRunRoundedIcon from '@mui/icons-material/DirectionsRunRounded';
import * as s from 'reacts/styles/components/project/Index';
const Index = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const params = useParams();
  const user = useSelector((state) => state.user);
  const [lastSprint, setLastSprint] = useState(null);
  const [inSprint, setInSprint] = useState(null);
  const [backlog, setBacklog] = useState(null);
  const [multiSelect, setMultiSelect] = useState([]);
  const [noIssue, setNoIssue] = useState(false);
  const [deprecated, setDeprecated] = useState(false);
  const [onEdit, setOnEdit] = useState(false);
  const [title, setTitle] = useState('');
  const [goal, setGoal] = useState('');

  const handleCheck = (id) => {
    setMultiSelect((prev) => {
      if (prev.includes(id)) {
        return prev.filter((issueId) => issueId !== id);
      }

      return [...prev, id];
    });
  };

  const getLastSprint = () => {
    eetch
      .recentSprint({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: params.groundId })
      .then((res) => {
        const sprints = res.data;
        if (sprints.length === 0) {
          // setNewSprintReady(true);
        } else {
          const recent = sprints[sprints.length - 1];
          setLastSprint(recent);
          // setNewSprintReady(recent.status === 2);

          const endDate = new Date(`${recent.endDate}T23:59:59`);
          const now = new Date();

          setDeprecated(endDate < now);

          // switch (recent.status) {
          //   case 0:
          //     if (endDate < now) {
          //       setStartSprintReady(false);
          //     } else setStartSprintReady(true);
          //     break;
          //   case 1:
          //   case 2:
          //   default:
          //     setStartSprintReady(false);
          //     break;
          // }
        }
      })
      .catch((err) => {
        if (err.message === 'RefreshTokenExpired') {
          dispatch(logoutUser());
          dispatch(setMenu(false));
          dispatch(setMessage(false));
          navigate(`/login`);
        }
      });
  };

  const connectSprint = (sprintId, issueId) => {
    eetch
      .connectSprint({
        accessToken: user.accessToken,
        refreshToken: user.refreshToken,
        groundId: params.groundId,
        sprintId,
        issueId,
      })
      .then(() => {
        getLastSprint();
      })
      .catch((err) => {
        if (err.message === 'RefreshTokenExpired') {
          dispatch(logoutUser());
          dispatch(setMenu(false));
          dispatch(setMessage(false));
          navigate(`/login`);
        }
      });
  };

  const multiConnectSprint = (sprintId) => {
    if (multiSelect.length > 1) {
      setMultiSelect([]);
      eetch
        .multiConnectSprint({
          accessToken: user.accessToken,
          refreshToken: user.refreshToken,
          groundId: params.groundId,
          sprintId,
          issueList: multiSelect,
        })
        .then(() => {
          getLastSprint();
        })
        .catch((err) => {
          if (err.message === 'RefreshTokenExpired') {
            dispatch(logoutUser());
            dispatch(setMenu(false));
            dispatch(setMessage(false));
            navigate(`/login`);
          }
        });
    }
  };

  const toggleSprint = (issueId) => {
    eetch
      .issueToggle({
        accessToken: user.accessToken,
        refreshToken: user.refreshToken,
        groundId: params.groundId,
        issueId,
      })
      .then(() => {
        console.log('toggle');
        getLastSprint();
      })
      .catch((err) => {
        if (err.message === 'RefreshTokenExpired') {
          dispatch(logoutUser());
          dispatch(setMenu(false));
          dispatch(setMessage(false));
          navigate(`/login`);
        }
      });
  };

  const createSprint = () => {
    eetch
      .createSprint({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: params.groundId })
      .then(() => {
        getLastSprint();
      })
      .catch((err) => {
        if (err.message === 'RefreshTokenExpired') {
          dispatch(logoutUser());
          dispatch(setMenu(false));
          dispatch(setMessage(false));
          navigate(`/login`);
        }
      });
  };

  const editSprint = () => {
    if (title === '') setTitle(lastSprint.name);

    eetch
      .editSprint({
        accessToken: user.accessToken,
        refreshToken: user.refreshToken,
        groundId: params.groundId,
        sprintId: lastSprint.id,
        goal,
        name: title === '' ? lastSprint.name : title,
      })
      .then(() => {
        getLastSprint();
      })
      .catch((err) => {
        if (err.message === 'RefreshTokenExpired') {
          dispatch(logoutUser());
          dispatch(setMenu(false));
          dispatch(setMessage(false));
          navigate(`/login`);
        }
      });
  };

  const deleteSprint = () => {
    eetch
      .editDocument({
        accessToken: user.accessToken,
        refreshToken: user.refreshToken,
        groundId: params.groundId,
        sprintId: lastSprint.id,
      })
      .then(() => {
        getLastSprint();
      })
      .catch((err) => {
        if (err.message === 'RefreshTokenExpired') {
          dispatch(logoutUser());
          dispatch(setMenu(false));
          dispatch(setMessage(false));
          navigate(`/login`);
        }
      });
  };

  const startSprint = () => {
    if (inSprint.length === 0) setNoIssue(true);
    else
      eetch
        .startSprint({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: params.groundId, sprintId: lastSprint.id })
        .then(() => {
          getLastSprint();
        })
        .catch((err) => {
          if (err.message === 'RefreshTokenExpired') {
            dispatch(logoutUser());
            dispatch(setMenu(false));
            dispatch(setMessage(false));
            navigate(`/login`);
          }
        });
  };

  const completeSprint = () => {
    eetch
      .completeSprint({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: params.groundId, sprintId: lastSprint.id })
      .then(() => {
        getLastSprint();
      })
      .catch((err) => {
        if (err.message === 'RefreshTokenExpired') {
          dispatch(logoutUser());
          dispatch(setMenu(false));
          dispatch(setMessage(false));
          navigate(`/login`);
        }
      });
  };

  useEffect(() => {
    if (lastSprint) {
      setTitle(lastSprint.name);
      setGoal(lastSprint.goal);
    }

    eetch
      .sprintIssues({
        accessToken: user.accessToken,
        refreshToken: user.refreshToken,
        groundId: params.groundId,
        sprintId: lastSprint === null ? -1 : lastSprint.id,
      })
      .then((res) => {
        const issuesInSprint = [];
        const issuesInBacklog = [];
        res.data.forEach((issue) => {
          if (issue.sprintId) issuesInSprint.push(issue);
          else issuesInBacklog.push(issue);
        });

        setInSprint(issuesInSprint);
        setBacklog(issuesInBacklog);
      })
      .catch((err) => {
        if (err.message === 'RefreshTokenExpired') {
          dispatch(logoutUser());
          dispatch(setMenu(false));
          dispatch(setMessage(false));
          navigate(`/login`);
        }
      });
  }, [lastSprint]);

  useEffect(() => {
    if (params.groundId) getLastSprint();
  }, [params.groundId]);

  const IssueRow = ({ issue, type, handleCheck, multiSelect }) => {
    const [checked, setChecked] = useState(false);

    console.log(issue.id);

    return (
      <s.IssueRow $checked={multiSelect.includes(issue.id)} status={issue.status} type={type}>
        <td className="check-box">
          <CheckBoxRoundedIcon
            onClick={() => {
              setChecked(!checked);
              handleCheck(issue.id);
            }}
          />
        </td>
        <td
          className="issue-title"
          onClick={() => {
            navigate(`/${params.groundId}/document/find/docs/issue/${issue.id}`);
          }}
        >
          {issue.title === '' ? '새 문서' : issue.title}
        </td>
        <td className="issue-status">
          <span onClick={() => toggleSprint(issue.id)}>{issue.status === 0 ? '해야 할 일' : issue.status === 1 ? '진행 중' : '완료'}</span>
        </td>
        <td className="focus-time">
          <span>{issue.focusTime}</span>
        </td>
        <td className="modifier">{issue.author}</td>
        {type === 'inIssue' ? (
          <s.ConnectWrapper className="connect-button" $available={multiSelect.length > 0}>
            <s.ConnectButton onClick={() => connectSprint(0, issue.id)}>
              <RemoveCircleOutlineRoundedIcon />
            </s.ConnectButton>
          </s.ConnectWrapper>
        ) : (
          <s.ConnectWrapper className="connect-button" $available={multiSelect.length > 0}>
            <s.ConnectButton onClick={() => connectSprint(lastSprint.id, issue.id)}>
              <AddCircleOutlineRoundedIcon />
            </s.ConnectButton>
          </s.ConnectWrapper>
        )}
      </s.IssueRow>
    );
  };

  return (
    <s.SprintWrapper>
      <s.RecentSprint>
        {lastSprint === null ? (
          <>
            <s.ButtonWrapper>
              <s.CreateButton onClick={createSprint}>
                <FlagRoundedIcon />
                스프린트 생성
              </s.CreateButton>
            </s.ButtonWrapper>
            <s.ThereIsNothing>
              기존 스프린트가 없습니다.
              <br />
              새로운 스프린트를 시작해보세요.
            </s.ThereIsNothing>
          </>
        ) : (
          <>
            <s.InfoWrapper>
              {onEdit ? (
                <s.TitleInput
                  value={title}
                  placeholder="스프린트 명을 작성해 주세요."
                  onChange={(event) => {
                    setTitle(event.target.value);
                  }}
                />
              ) : (
                <h1>{lastSprint.name}</h1>
              )}
            </s.InfoWrapper>
            {lastSprint.goal && !onEdit && (
              <s.GoalWrapper>
                <p>{lastSprint.goal}</p>
              </s.GoalWrapper>
            )}
            {onEdit && (
              <s.GoalInput
                value={goal}
                placeholder="목표를 설정해 주세요."
                onChange={(event) => {
                  setGoal(event.target.value);
                }}
              />
            )}
            <s.ButtonWrapper>
              {lastSprint.status === 0 ? (
                deprecated ? (
                  <s.StartButton onClick={deleteSprint}>
                    <RemoveCircleRoundedIcon />
                    스프린트 삭제
                  </s.StartButton>
                ) : (
                  <s.StartButton onClick={startSprint}>
                    <DirectionsRunRoundedIcon />
                    스프린트 시작
                  </s.StartButton>
                )
              ) : lastSprint.status === 1 ? (
                <s.EndButton onClick={completeSprint}>
                  <BlindsClosedRoundedIcon />
                  스프린트 종료
                </s.EndButton>
              ) : (
                <s.CreateButton onClick={createSprint}>
                  <FlagRoundedIcon />
                  스프린트 생성
                </s.CreateButton>
              )}
              <s.EditButton
                $onEdit={onEdit}
                onClick={() => {
                  if (onEdit) editSprint();
                  setOnEdit(!onEdit);
                }}
              >
                <DriveFileRenameOutlineRoundedIcon />
                제목|목표 수정 {onEdit && '완료'}
              </s.EditButton>
              {multiSelect.length > 0 && (
                <>
                  <s.SubmitButton onClick={() => multiConnectSprint(0)}>
                    <ArrowCircleDownRoundedIcon /> 선택 이동
                  </s.SubmitButton>
                  <s.DeleteButton
                    onClick={() => {
                      setMultiSelect([]);
                    }}
                  >
                    <HighlightOffRoundedIcon />
                    선택 취소
                  </s.DeleteButton>
                </>
              )}
            </s.ButtonWrapper>

            {inSprint !== null && inSprint.length > 0 ? (
              <s.IssueTable status={lastSprint.status}>
                <s.IssueBody sprintstatus={lastSprint.status}>
                  {inSprint.map((issue) => (
                    <IssueRow key={issue.id} issue={issue} type="inIssue" handleCheck={handleCheck} multiSelect={multiSelect} />
                  ))}
                </s.IssueBody>
              </s.IssueTable>
            ) : (
              <s.ThereIsNothing>등록된 이슈가 존재하지 않습니다.</s.ThereIsNothing>
            )}
          </>
        )}
      </s.RecentSprint>
      <s.Backlog>
        <s.InfoWrapper>
          <h1>백로그</h1>
        </s.InfoWrapper>
        <s.ButtonWrapper>
          {multiSelect.length > 0 && (
            <>
              <s.SubmitButton onClick={() => multiConnectSprint(lastSprint.id)}>
                <ArrowCircleUpRoundedIcon /> 선택 이동
              </s.SubmitButton>
              <s.DeleteButton
                onClick={() => {
                  setMultiSelect([]);
                }}
              >
                <HighlightOffRoundedIcon />
                선택 취소
              </s.DeleteButton>
            </>
          )}
        </s.ButtonWrapper>

        {backlog !== null && backlog.length > 0 ? (
          <s.IssueTable>
            <s.IssueBody sprintstatus={lastSprint ? lastSprint.status : 2}>
              {backlog.map((issue) => (
                <IssueRow key={issue.id} issue={issue} type="backlog" handleCheck={handleCheck} multiSelect={multiSelect} />
              ))}
            </s.IssueBody>
          </s.IssueTable>
        ) : (
          <s.ThereIsNothing>등록된 이슈가 존재하지 않습니다.</s.ThereIsNothing>
        )}
      </s.Backlog>
      <Modal
        isOpen={noIssue}
        type="alarm"
        message="* 해당 스프린트에 이슈가 존재하지 않습니다."
        onRequestClose={() => {
          setNoIssue(false);
        }}
      />
    </s.SprintWrapper>
  );
};

export default Index;
