import styled from 'styled-components';

export const EditorWrapper = styled.div`
  position: relative;
  min-width: 340px;
  max-width: 960px;

  margin: 0 auto;

  background-color: var(--bg-basic);

  & > textarea {
    width: calc(100% - 30px);
    height: fit-content;
    margin: 60px 15px 0 15px;

    color: var(--font-rev);
    font-size: 2rem;
    font-weight: 600;

    background-color: transparent;
    border: none;
    outline: none;
    resize: none;

    font-family:
      Pretendard,
      -apple-system,
      BlinkMacSystemFont,
      system-ui,
      Roboto,
      'Helvetica Neue',
      'Segoe UI',
      'Apple SD Gothic Neo',
      'Noto Sans KR',
      'Malgun Gothic',
      'Apple Color Emoji',
      'Segoe UI Emoji',
      'Segoe UI Symbol',
      sans-serif;

    //입력된 텍스트가 넓이를 넘어가면 다음 줄로 넘어감
    overflow: hidden;
    white-space: pre-line;
  }

  .quill {
    padding-top: 0px;
    pointer-events: ${({ status }) => (status === 0 ? 'auto' : 'none')};
  }

  .ql-toolbar.ql-snow {
    width: 375px;
    padding-bottom: 20px;

    display: ${({ status }) => (status === 0 ? 'block' : 'none')};
  }

  .ql-toolbar .ql-stroke {
    fill: none;
    stroke: var(--font-rev);
  }

  .ql-toolbar,
  .ql-fill {
    fill: var(--font-rev);
    stroke: none;
  }

  .ql-toolbar .ql-picker {
    color: var(--font-rev);
  }

  .ql-toolbar,
  .ql-container {
    border: none;

    .user-cursor {
      padding: 5px 10px;
      transform: translate(-49%, -30px);

      color: var(--font-rev);
      background-color: var(--font-basic);
      border-radius: 6px;

      white-space: nowrap;
      box-shadow: rgba(0, 0, 0, 0.2) 0 2px 8px;

      transition: left 0.1s cubic-bezier(0.08, 0.84, 0.8, 0.75);

      & > .user-cursor-mark {
        position: absolute;
        bottom: -13px;
        left: 50%;
        height: 0px;
        width: 0px;
        transform: translateX(-50%);

        border-top: 7px solid var(--border-cursor);
        border-right: 7px solid transparent;
        border-bottom: 7px solid transparent;
        border-left: 7px solid transparent;

        box-shadow: rgba(0, 0, 0, 0.2) 0 2px 8px;
      }
    }
  }

  .ql-editor,
  .ql-header {
    color: var(--font-rev);
    font-family:
      Pretendard,
      -apple-system,
      BlinkMacSystemFont,
      system-ui,
      Roboto,
      'Helvetica Neue',
      'Segoe UI',
      'Apple SD Gothic Neo',
      'Noto Sans KR',
      'Malgun Gothic',
      'Apple Color Emoji',
      'Segoe UI Emoji',
      'Segoe UI Symbol',
      sans-serif;
  }

  .ql-picker-options {
    background-color: var(--bg-black);
    border-radius: 6px;
    transform: translateY(10px);
  }

  .ql-toolbar.ql-snow .ql-picker-options {
    box-shadow: rgba(0, 0, 0, 0.2) 0 2px 8px;
  }

  .ql-picker-label > svg {
    display: none;
  }

  .ql-formats:first-child,
  .ql-picker-label {
    font-size: 0.9rem;
    width: 90px;
    height: 28px;
  }

  .ql-formats:last-child {
    margin: 0;
  }

  .ql-formats {
    & > button {
      width: 35px;
      height: 28px;
    }

    & > .ql-picker {
      margin-top: 2px;
    }
  }

  .ql-toolbar.ql-snow .ql-formats {
    margin-right: 0;
  }

  button {
    margin-left: 3px;

    & > svg {
      margin-left: 2px;
    }

    :hover {
      color: var(--font-rev);
    }
  }

  .insert-button {
    margin: 0 15px;
  }

  .ql-snow.ql-toolbar button.ql-active {
    background: var(--button-lite);
    border-radius: 6px;
  }

  .ql-snow.ql-toolbar button:hover,
  .ql-snow .ql-toolbar button:hover,
  .ql-snow.ql-toolbar .ql-picker-label:hover,
  .ql-snow .ql-toolbar .ql-picker-label:hover,
  .ql-snow.ql-toolbar .ql-picker-item:hover,
  .ql-snow .ql-toolbar .ql-picker-item:hover {
    color: var(--font-hover);
  }

  .ql-snow.ql-toolbar .ql-picker-label.ql-active,
  .ql-snow .ql-toolbar .ql-picker-label.ql-active,
  .ql-snow.ql-toolbar button.ql-active,
  .ql-snow .ql-toolbar button.ql-active,
  .ql-snow.ql-toolbar button:focus,
  .ql-snow .ql-toolbar button:focus,
  .ql-snow.ql-toolbar .ql-picker-item.ql-selected,
  .ql-snow .ql-toolbar .ql-picker-item.ql-selected,
  .ql-snow.ql-toolbar .ql-picker-label:before,
  .ql-snow .ql-toolbar .ql-picker-label:before {
    color: var(--font-rev);
  }

  .ql-snow.ql-toolbar button:hover .ql-fill,
  .ql-snow .ql-toolbar button:hover .ql-fill,
  .ql-snow.ql-toolbar button:focus .ql-fill,
  .ql-snow .ql-toolbar button:focus .ql-fill,
  .ql-snow.ql-toolbar button.ql-active .ql-fill,
  .ql-snow .ql-toolbar button.ql-active .ql-fill,
  .ql-snow.ql-toolbar .ql-picker-label:hover .ql-fill,
  .ql-snow .ql-toolbar .ql-picker-label:hover .ql-fill,
  .ql-snow.ql-toolbar .ql-picker-label.ql-active .ql-fill,
  .ql-snow .ql-toolbar .ql-picker-label.ql-active .ql-fill,
  .ql-snow.ql-toolbar .ql-picker-item:hover .ql-fill,
  .ql-snow .ql-toolbar .ql-picker-item:hover .ql-fill,
  .ql-snow.ql-toolbar .ql-picker-item.ql-selected .ql-fill,
  .ql-snow .ql-toolbar .ql-picker-item.ql-selected .ql-fill,
  .ql-snow.ql-toolbar button:hover .ql-stroke.ql-fill,
  .ql-snow .ql-toolbar button:hover .ql-stroke.ql-fill,
  .ql-snow.ql-toolbar button:focus .ql-stroke.ql-fill,
  .ql-snow .ql-toolbar button:focus .ql-stroke.ql-fill,
  .ql-snow.ql-toolbar button.ql-active .ql-stroke.ql-fill,
  .ql-snow .ql-toolbar button.ql-active .ql-stroke.ql-fill,
  .ql-snow.ql-toolbar .ql-picker-label:hover .ql-stroke.ql-fill,
  .ql-snow .ql-toolbar .ql-picker-label:hover .ql-stroke.ql-fill,
  .ql-snow.ql-toolbar .ql-picker-label.ql-active .ql-stroke.ql-fill,
  .ql-snow .ql-toolbar .ql-picker-label.ql-active .ql-stroke.ql-fill,
  .ql-snow.ql-toolbar .ql-picker-item:hover .ql-stroke.ql-fill,
  .ql-snow .ql-toolbar .ql-picker-item:hover .ql-stroke.ql-fill,
  .ql-snow.ql-toolbar .ql-picker-item.ql-selected .ql-stroke.ql-fill,
  .ql-snow .ql-toolbar .ql-picker-item.ql-selected .ql-stroke.ql-fill {
    fill: var(--font-rev);
  }

  .ql-snow.ql-toolbar button:hover .ql-stroke,
  .ql-snow .ql-toolbar button:hover .ql-stroke,
  .ql-snow.ql-toolbar button:focus .ql-stroke,
  .ql-snow .ql-toolbar button:focus .ql-stroke,
  .ql-snow.ql-toolbar button.ql-active .ql-stroke,
  .ql-snow .ql-toolbar button.ql-active .ql-stroke {
    stroke: var(--font-rev);
  }

  .ql-toolbar.ql-snow .ql-picker.ql-expanded .ql-picker-label {
    border: 1px solid transparent;
    .ql-formats:first-child {
      background-color: 000;
    }
  }

  .ql-toolbar.ql-snow .ql-picker.ql-expanded .ql-picker-options {
    border: 1px solid transparent;
  }

  /* .ql-snow.ql-toolbar button:hover,
  .ql-snow .ql-toolbar button:hover {
    color: #fff;
  } */

  .ql-editor.ql-blank::before {
    left: 15px;
    right: 15px;
    pointer-events: none;
    position: absolute;

    color: var(--font-editor-placeholder);
    font-size: 1rem;
  }

  .ql-editor {
    padding-bottom: 0;

    p {
      font-weight: 300;
    }

    * {
      font-size: 1rem;
      margin: 0 0 15px 0;
      line-height: 1.5;
    }

    h1 {
      font-size: 3rem;
    }

    h2 {
      font-size: 2.4rem;
    }

    h3 {
      font-size: 2rem;
    }

    h4 {
      font-size: 1.6rem;
    }

    h5 {
      font-size: 1.2rem;
    }

    h6 {
      font-size: 1rem;
    }

    hr {
      border: 1px solid var(--hr-basic);
    }

    pre {
      font-size: 0.9rem;
      & > span {
        font-size: 0.9rem;
      }
      font-family: 'consolas', 'gulim';
      width: auto;
      margin: 0px 10px 15px 10px;

      overflow-x: auto;

      padding: 30px 20px 30px 20px;
      border-radius: 4px;

      box-shadow:
        rgba(0, 0, 0, 0.24) 0px 1px 1px,
        rgba(0, 0, 0, 0.24) 0px 2px 2px,
        rgba(0, 0, 0, 0.24) 0px 4px 6px;

      /* &::before {
        width: 10px;
      } */

      &::-webkit-scrollbar {
        height: 9px;
      }
      &::-webkit-scrollbar-thumb {
        background-clip: padding-box;
        border-left: 20px solid transparent;
        border-right: 20px solid transparent;
        border-top: 3px solid transparent;
        border-bottom: 3px solid transparent;
      }

      &:hover {
        border-radius: 0px 3px 3px 0px;
      }
    }

    blockquote {
      width: auto;
      margin: 0px 10px 15px 10px;

      padding: 10px 20px 10px 20px;
      border: none;
      border-radius: 4px 4px 24px 4px;

      background-color: var(--blockquote-bg);

      transition: background-color 0.2s ease;

      box-shadow:
        rgba(0, 0, 0, 0.24) 0px 1px 1px,
        rgba(0, 0, 0, 0.24) 0px 2px 2px,
        rgba(0, 0, 0, 0.24) 0px 4px 6px;

      /* &::before {
        width: 10px;
      } */

      &:hover {
        background-color: var(--blockquote-hover);

        border-radius: 0px 3px 24px 0px;
      }
    }

    p > code,
    h1 > code,
    h2 > code,
    h3 > code,
    h4 > code,
    h5 > code,
    h6 > code,
    ul > code,
    ol > code,
    dl > code,
    li > code,
    table > code,
    blockquote > code {
      padding: 3px 6px;
      margin: 0 2px;

      color: var(--code-highlight-font);
      font-family: inherit;
      font-weight: inherit;
      font-size: inherit;

      background-color: var(--code-highlight-bg);
      border-radius: 5px;
    }

    li {
      margin: 10px 0;

      > * {
        margin: 10px 0;
      }
    }

    * {
      position: relative;
    }
  }
`;

export const InsertBottom = styled.button`
  position: relative;
  width: calc(100% - 30px);
  max-width: 930px;

  display: block;
  margin: 0 15px;
  margin-bottom: 50px;
  padding: 4px 10px 4px 10px;

  color: var(--font-rev);

  border: none;
  border-radius: 6px;
  background-color: var(--button-darker);

  box-shadow:
    rgba(0, 0, 0, 0.24) 0px 1px 1px,
    rgba(0, 0, 0, 0.24) 0px 2px 2px,
    rgba(0, 0, 0, 0.24) 0px 4px 6px;

  transition: background-color 0.15s ease;

  cursor: pointer;
  pointer-events: ${({ status }) => (status === 0 ? 'auto' : 'none')};
  opacity: ${({ status }) => (status === 0 ? '1' : '0.3')};

  &:hover {
    background-color: var(--button-lite);
  }
`;

export const SettingButton = styled.button`
  position: absolute;
  right: 15px;
  top: 17px;

  padding: 0;
  margin: 0;
  border-radius: 0.5rem;
  display: flex;
  align-items: center;

  font-size: 1rem;

  background-color: transparent;
  border: none;
  z-index: 6;
  transition: background-color 0.15s ease;

  &:hover {
    background-color: var(--button-lite);

    & > svg {
      fill: var(--font-rev);
    }
  }

  & > svg {
    fill: var(--font-border);
    margin: 2px;
  }

  cursor: pointer;

  &.delete-button {
    right: 55px;

    opacity: ${({ $available }) => ($available ? '0.5' : '1')};
    pointer-events: ${({ $available }) => ($available ? 'none' : 'auto')};

    & > svg {
      fill: var(--font-border);
      margin: 3px;
      width: 25px;
      height: 25px;
    }

    &:hover {
      & > svg {
        fill: var(--font-delete);
      }
    }
  }

  &.only-delete-button {
    & > svg {
      fill: var(--font-border);
      margin: 3px;
      width: 25px;
      height: 25px;
    }

    &:hover {
      & > svg {
        fill: var(--font-delete);
      }
    }
  }
`;

export const SettingWrapper = styled.div`
  position: absolute;
  top: 10px;

  width: calc(100% - 10px);
  height: fit-content;
  margin: 0 5px;
  background-color: var(--bg-black);

  border-radius: 0.75rem;
  z-index: 7;

  opacity: ${({ $toggle }) => ($toggle ? '1' : '0')};
  visibility: ${({ $toggle }) => ($toggle ? 'visible' : 'hidden')};

  transition:
    opacity 0.3s ease,
    visibility 0.3s ease;

  box-shadow:
    rgba(0, 0, 0, 0.24) 0px 1px 1px,
    rgba(0, 0, 0, 0.24) 0px 2px 2px,
    rgba(0, 0, 0, 0.24) 0px 4px 6px;

  & > .close-button {
    top: 7px;
    right: 10px;
    & > svg {
      fill: var(--font-border);
      width: 25px;
      height: 25px;
      margin: 3px;
    }

    &:hover {
      & > svg {
        fill: var(--font-rev);
      }
    }
  }

  & > textarea {
    width: calc(100% - 20px);
    height: fit-content;
    margin: 50px 10px 0 10px;

    color: var(--font-rev);
    font-size: 2rem;
    font-weight: 600;

    background-color: transparent;
    border: none;
    outline: none;
    resize: none;

    font-family:
      Pretendard,
      -apple-system,
      BlinkMacSystemFont,
      system-ui,
      Roboto,
      'Helvetica Neue',
      'Segoe UI',
      'Apple SD Gothic Neo',
      'Noto Sans KR',
      'Malgun Gothic',
      'Apple Color Emoji',
      'Segoe UI Emoji',
      'Segoe UI Symbol',
      sans-serif;

    //입력된 텍스트가 넓이를 넘어가면 다음 줄로 넘어감
    overflow: hidden;
    white-space: pre-line;
  }
`;

export const SettingLabel = styled.p`
  left: 13px;
  top: 17px;
  position: absolute;
  color: var(--font-border);

  font-weight: 500;
  font-size: 0.9rem;
`;

export const DivLine = styled.div`
  width: calc(100% - 30px);
  margin: 10px 15px 25px 15px;
  height: 1px;
  background-color: var(--font-border);
  opacity: 0.25;
`;

export const ParentWrapper = styled.div`
  margin-top: 20px;
  & > div {
    margin-bottom: 0px;

    & > div {
      margin: 0 15px;
      width: calc(100% - 30px);

      & > svg {
        right: 25px;
      }
      &:focus {
        background-color: #fff;
      }
    }
  }
`;

export const AttributeWrapper = styled.div`
  & > div {
    margin-bottom: 30px;
    & > div {
      margin: 0 15px;
      width: calc(100% - 30px);

      & > svg {
        right: 25px;
      }
      &:focus {
        background-color: #fff;
      }
    }

    & > h2 {
      left: 30px;
    }

    & > input {
      margin: 0 15px;
      width: calc(100% - 30px);
    }
  }
`;

export const RequestWrapper = styled.div`
  position: relative;
  & > div {
    margin-bottom: 0;
    & > div {
      margin: 0 15px;
      width: calc(100% - 30px);

      & > svg {
        right: 25px;
      }
      &:focus {
        background-color: #fff;
      }
    }

    & > h2 {
      left: 30px;
    }

    & > input {
      margin: 0 15px;
      width: calc(100% - 30px);
    }
  }
`;

export const StatusText = styled.h3`
  position: absolute;
  top: 22px;
  right: 103px;

  font-weight: 400;
  font-size: 1rem;

  color: var(--font-border);
`;

export const ProfileImage = styled.img`
  width: 90px;
  height: 30px;

  margin-right: 10px;
  border: 1px solid var(--border-dark);

  //비율 유지
  object-fit: cover;

  transition: filter 0.3s ease;
  border-radius: 25px;
`;

export const ChargeInfo = styled.span`
  position: absolute;
  top: 17px;
  left: 18px;

  width: calc(100% - 190px);

  display: flex;
  align-items: center;

  & > svg {
    display: inline;
    margin: 0 10px;

    opacity: 0.5;
  }
`;

export const SenderInfo = styled.span`
  display: flex;
  align-items: center;
`;
export const ReceiverInfo = styled.span`
  display: flex;
  align-items: center;

  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
`;

export const RequestDescription = styled.p`
  position: absolute;

  font-size: 0.8rem;
  font-weight: 300;
  color: var(--font-rev);
  opacity: 0.5;

  top: -20px;
  left: 25px;

  padding: 0;
  margin: 0;
`;

export const CommentWrapper = styled.div`
  position: relative;
  width: 100%;
  margin: 100px 0;
  padding: 10px 0 15px 0;

  border-radius: 0.75rem;

  background-color: var(--bg-lite);

  display: ${({ status }) => (status !== 0 ? 'block' : 'none')};

  box-shadow:
    rgba(0, 0, 0, 0.24) 0px 1px 1px,
    rgba(0, 0, 0, 0.24) 0px 2px 2px,
    rgba(0, 0, 0, 0.24) 0px 4px 6px;

  .quill {
    padding-top: 0px;
  }

  .ql-toolbar.ql-snow {
    width: 375px;
    padding-bottom: 20px;
    display: block;

    opacity: 1;
  }

  & > button {
    pointer-events: auto;
    opacity: 1;

    background-color: var(--button-comment);
  }

  & > svg {
    left: 50%;
    top: -70px;
    transform: translateX(-50%);
    position: absolute;
    width: 64px;
    height: 64px;

    border-radius: 0.75rem;

    cursor: pointer;

    &:hover {
      background-color: var(--button-lite);
    }
  }
`;

export const CommentsWrapper = styled.div`
  position: relative;
  width: 100%;
  margin: 20px 0;
  padding: 50px 0 15px 0;

  border-radius: 0.75rem;
  background-color: #070a0d;
  border-radius: 0.75rem;

  .quill {
    padding-top: 0px;
  }

  .ql-toolbar.ql-snow {
    display: none;
  }

  box-shadow:
    rgba(0, 0, 0, 0.24) 0px 1px 1px,
    rgba(0, 0, 0, 0.24) 0px 2px 2px,
    rgba(0, 0, 0, 0.24) 0px 4px 6px;
`;

export const CommentAuthor = styled.span`
  position: absolute;
  top: 23px;
  left: 115px;
  display: flex;
  align-items: center;
  font-size: 0.9rem;
  font-weight: 300;

  opacity: 0.7;
`;

export const CommentImage = styled.img`
  position: absolute;
  top: 17px;
  left: 15px;
  width: 90px;
  height: 30px;

  margin-right: 10px;
  border: 1px solid var(--border-dark);

  //비율 유지
  object-fit: cover;

  transition: filter 0.3s ease;
  border-radius: 25px;
`;
