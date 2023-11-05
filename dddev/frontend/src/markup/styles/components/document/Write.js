import styled from 'styled-components';

export const EditorWrapper = styled.div`
  position: relative;
  min-width: 340px;
  max-width: 960px;

  margin: 0 auto;

  background-color: var(--bg-basic);

  .quill {
    padding-top: 50px;
  }

  .ql-toolbar.ql-snow {
    width: 375px;
    padding-bottom: 20px;
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

  .ql-snow.ql-toolbar button.ql-active {
    background: var(--button-activated);
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
        rgba(0, 0, 0, 0.24) 0px 4px 4px;

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
        rgba(0, 0, 0, 0.24) 0px 4px 4px;

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

      /* &::before {
        content: '';
        position: absolute;
        top: 0;
        left: -10px;
        width: 6px;
        height: 100%;
        background-color: var(--font-rev);

        transition: opacity 0.2s ease;

        opacity: 0;
      }

      &:hover::before {
        opacity: 1;
      } */
    }

    /* code {
      &:hover::before {
        opacity: 0;
      }
    } */
  }
`;

export const InsertBottom = styled.button`
  position: relative;
  width: calc(100% - 30px);
  max-width: 930px;

  display: block;
  margin: 0 auto;
  padding: 4px 10px 4px 10px;

  color: var(--font-rev);

  border: none;
  border-radius: 6px;
  background-color: var(--button-basic);

  box-shadow:
    rgba(0, 0, 0, 0.24) 0px 1px 1px,
    rgba(0, 0, 0, 0.24) 0px 2px 2px,
    rgba(0, 0, 0, 0.24) 0px 4px 4px;

  transition: background-color 0.15s ease;

  cursor: pointer;

  &:hover {
    background-color: var(--button-lite);
  }
`;
