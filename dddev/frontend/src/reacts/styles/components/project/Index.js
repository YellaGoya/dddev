import styled from 'styled-components';

export const SprintWrapper = styled.div`
  & > section {
    width: 100%;
    min-width: 340px;
    max-width: 960px;
    padding: 15px 10px;
    border-radius: 0.75rem;
  }
`;
export const RecentSprint = styled.section`
  background-color: var(--bg-lite);
  border: 1px solid var(--border-basic);
  margin: 0 auto;
  margin-top: 30px;
  margin-bottom: 10px;
`;
export const InfoWrapper = styled.div`
  & > h1 {
    font-size: 1.1rem;
    padding: 0 10px;
  }
`;

export const GoalWrapper = styled.div`
  margin-top: 5px;
  margin-bottom: 15px;

  & > p {
    font-size: 0.9rem;
    font-weight: 400;
    padding: 0 10px;
    margin: 0;
  }
`;

export const ButtonWrapper = styled.div`
  position: relative;
  margin-top: 8px;
  & > button {
    border: none;
    background-color: transparent;

    padding: 5px 10px;
    border-radius: 0.5rem;

    font-size: 0.8rem;
    font-weight: 400;

    transition: background-color 0.2s ease;

    cursor: pointer;

    &:hover {
      background-color: var(--button-hover);
    }

    display: flex;
    align-items: center;
    & > svg {
      width: 21px;
      height: 21px;
      margin-right: 4px;
    }
  }
`;

export const CreateButton = styled.button`
  display: inline-block;
`;
export const StartButton = styled.button`
  display: inline-block;
`;
export const EndButton = styled.button`
  display: inline-block;
`;
export const EditButton = styled.button`
  display: inline-block;
  color: ${({ $onEdit }) => ($onEdit ? 'var(--font-new)' : 'var(--font-rev)')};
  & > svg {
    transition: none;
    fill: ${({ $onEdit }) => ($onEdit ? 'var(--font-new)' : 'var(--font-rev)')};
  }
`;

export const Backlog = styled.section`
  margin: 0 auto;

  border: 1px solid var(--border-basic);
`;

export const IssueTable = styled.table`
  width: calc(100%);
  margin-top: 45px;
  list-style: none;
  border: none;

  border-collapse: collapse;

  opacity: ${(props) => (props.status === 2 ? '0.35' : '1')};

  & > tbody > tr {
    &:hover {
      background-color: ${(props) => (props.status === 2 ? 'transparent' : 'var(--bg-black)')};
    }

    & > td.issue-status {
      pointer-events: ${(props) => (props.status === 2 ? 'none' : 'auto')};
    }
  }
`;

export const IssueBody = styled.tbody`
  & > tr {
    & > div {
      display: ${(props) => (props.sprintstatus === 0 ? 'block' : 'none')};
    }

    & > td.check-box {
      display: ${(props) => (props.sprintstatus === 0 ? 'block' : 'none')};
    }

    & > td.issue-title {
      padding-left: ${(props) => (props.sprintstatus === 0 ? '35px' : '10px')};
    }

    & > td.modifier {
      padding-right: ${(props) => (props.sprintstatus === 0 ? '40px' : '10px')};
    }
  }
`;

export const IssueRow = styled.tr`
  width: 100%;
  position: relative;
  height: 31px;

  transition: background-color 0.2s ease;

  &:hover {
    background-color: var(--bg-black);

    & > td:first-child {
      border-radius: 0.75rem 0 0 0.75rem;
    }

    & > td.check-box {
      opacity: 0.5;
    }
  }

  & > td {
    padding: 0 10px;
    font-size: 0.9rem;
    text-align: center;

    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;

    border: none;
  }

  & > td.check-box {
    position: absolute;
    top: 4px;
    left: 10px;
    padding: 0;
    opacity: ${({ $checked }) => ($checked ? '1' : '0')};
    transition: opacity 0.2s ease;

    &:hover {
      opacity: 1;
    }

    & > svg {
      fill: ${({ $checked }) => ($checked ? 'var(--font-toggle)' : 'var(--font-rev)')};

      width: 21px;
      height: 21px;

      cursor: pointer;
    }
  }

  @media (max-width: 600px) {
    & > td.check-box {
      opacity: 0.5;
    }
  }

  & > td.issue-status {
    font-size: 0.8rem;
    & > span {
      display: inline-block;

      margin: 0;
      padding: 3px 9px;
      font-size: 0.8rem;

      font-weight: ${(props) => (props.status === 2 ? '500' : '400')};
      color: ${(props) => (props.status === 2 ? 'var(--font-basic)' : 'var(--font-rev)')};

      border-radius: 0.25rem;

      background-color: ${(props) => (props.status === 0 ? 'var(--button-lite)' : props.status === 1 ? 'var(--font-new)' : 'var(--font-toggle)')};
    }
  }

  & > td.focus-time {
    & > span {
      display: inline-block;
      width: 23px;
      margin: 0;
      padding: 3px;
      font-size: 0.8rem;

      border-radius: 0.25rem;

      background-color: var(--button-lite);
    }
  }

  & > td.issue-title {
    border-radius: 0.5rem 0 0 0.5rem;
    min-width: 300px;
    width: 50%;
    text-align: left;

    cursor: pointer;

    &:hover {
      text-decoration: underline;
      text-underline-offset: 3px;
    }
  }

  & > td.modifier {
    border-radius: 0 0.5rem 0.5rem 0;
    width: 160px;
    text-align: right;
    padding-right: 40px;
  }

  @media (max-width: 650px) {
    & > td.issue-title {
      min-width: calc(100vw - 350px);
      width: 40%;
    }
  }

  @media (max-width: 550px) {
    & > td.focus-time {
      display: none;
    }
  }

  @media (max-width: 500px) {
    & > td.modifier {
      max-width: calc(100vw - 300px);
    }
  }

  @media (max-width: 480px) {
    & > td.issue-title {
      min-width: 180px;
      width: 40%;
    }
  }
`;

export const ConnectWrapper = styled.td`
  display: ${({ $available }) => ($available ? 'none' : 'block')};
  position: absolute;
  top: 2px;
  right: -3px;
`;

export const DeleteButton = styled.button`
  position: absolute;
  bottom: -40px;
  right: 10px;

  opacity: 0.5;

  transition: opacity 0.2s ease;

  &:hover {
    opacity: 1;
  }

  color: var(--font-toggle);
  & > svg {
    fill: var(--font-toggle);
  }
`;

export const SubmitButton = styled.button`
  position: absolute;
  bottom: -40px;
  right: 100px;

  color: var(--font-toggle);
  & > svg {
    fill: var(--font-toggle);
  }
`;

export const ConnectButton = styled.button`
  padding: 3px;
  border: none;
  background-color: transparent;

  cursor: pointer;
  opacity: 0.5;

  transition: opacity 0.2s ease;

  &:hover {
    opacity: 1;
  }

  & > svg {
    width: 21px;
    height: 21px;
  }
`;

export const ThereIsNoSprint = styled.div`
  margin: 30px 0;
  text-align: center;
  font-size: 1.3rem;
  font-weight: 600;
  opacity: 0.5;
`;

export const ThereIsNothing = styled.div`
  margin: 30px 0;
  text-align: center;
  font-size: 1.3rem;
  font-weight: 600;
  opacity: 0.5;
`;

export const TitleInput = styled.input`
  height: 24px;
  background-color: transparent;

  padding: 0 10px;
  opacity: 0.5;

  font-size: 1.1rem;
  font-weight: 700;
  border: none;
  outline: none;
`;

export const GoalInput = styled.input`
  margin-top: 2px;
  margin-bottom: 5px;
  height: 24px;
  background-color: transparent;

  padding: 0 10px;
  opacity: 0.5;

  font-size: 0.9rem;
  font-weight: 400;
  border: none;
  outline: none;
`;
