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
    padding: 0 5px;
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

export const CreateButton = styled.button``;
export const StartButton = styled.button``;
export const EndButton = styled.button``;

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
`;

export const IssueBody = styled.tbody``;

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

    & > svg {
      opacity: 0.5;
    }
  }

  & > svg {
    opacity: ${({ $checked }) => ($checked ? '1' : '0')};
    fill: ${({ $checked }) => ($checked ? 'var(--font-toggle)' : 'var(--font-rev)')};
    position: absolute;
    top: 5px;
    left: 10px;
    width: 21px;
    height: 21px;

    transition: opacity 0.2s ease;

    cursor: pointer;

    &:hover {
      opacity: 1;
    }
  }

  @media (max-width: 600px) {
    & > svg {
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
    width: 40%;
    text-align: left;
    padding-left: 40px;
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

export const ConnectWrapper = styled.div`
  display: ${({ $available }) => ($available ? 'none' : 'block')};
  position: absolute;
  top: 2px;
  right: 7px;

  &:hover {
  }
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
