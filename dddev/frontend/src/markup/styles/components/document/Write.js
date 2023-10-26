import styled from 'styled-components';

export const EditorWrapper = styled.div`
  position: relative;
  min-width: 340px;
  max-width: 960px;

  margin: 0 auto;

  background-color: #333;

  .quill {
    padding-top: 50px;
    padding-bottom: 50px;
  }

  .ql-toolbar.ql-snow {
    width: 375px;
    padding-bottom: 20px;
  }

  .ql-toolbar .ql-stroke {
    fill: none;
    stroke: #fff;
  }

  .ql-toolbar,
  .ql-fill {
    fill: #fff;
    stroke: none;
  }

  .ql-toolbar .ql-picker {
    color: #fff;
  }

  .ql-toolbar,
  .ql-container {
    border: none;

    .user-cursor {
      padding: 5px 10px;
      transform: translate(-49%, -30px);

      color: #fff;
      background-color: #000;
      border-radius: 6px;

      white-space: nowrap;
      box-shadow: rgba(0, 0, 0, 0.2) 0 2px 8px;

      /* transition: left 0.1s ease-in-out; */

      & > .user-cursor-mark {
        position: absolute;
        bottom: -13px;
        left: 50%;
        height: 0px;
        width: 0px;
        transform: translateX(-50%);

        border-top: 7px solid #000;
        border-right: 7px solid transparent;
        border-bottom: 7px solid transparent;
        border-left: 7px solid transparent;

        box-shadow: rgba(0, 0, 0, 0.2) 0 2px 8px;
      }
    }
  }

  .ql-editor,
  .ql-header {
    color: #fff;
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
    background-color: #111;
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
      color: #fff;
    }
  }

  .ql-snow.ql-toolbar button.ql-active {
    background: #222;
    border-radius: 6px;
  }

  .ql-snow.ql-toolbar button:hover,
  .ql-snow .ql-toolbar button:hover,
  .ql-snow.ql-toolbar .ql-picker-label:hover,
  .ql-snow .ql-toolbar .ql-picker-label:hover,
  .ql-snow.ql-toolbar .ql-picker-item:hover,
  .ql-snow .ql-toolbar .ql-picker-item:hover {
    color: #aaa;
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
    color: #fff;
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
    fill: #fff;
  }

  .ql-snow.ql-toolbar button:hover .ql-stroke,
  .ql-snow .ql-toolbar button:hover .ql-stroke,
  .ql-snow.ql-toolbar button:focus .ql-stroke,
  .ql-snow .ql-toolbar button:focus .ql-stroke,
  .ql-snow.ql-toolbar button.ql-active .ql-stroke,
  .ql-snow .ql-toolbar button.ql-active .ql-stroke {
    stroke: #fff;
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

  .ql-snow.ql-toolbar button:hover,
  .ql-snow .ql-toolbar button:hover {
    color: #222;
  }

  .ql-editor.ql-blank::before {
    left: 15px;
    right: 15px;
    pointer-events: none;
    position: absolute;

    color: #888;
    font-size: 1rem;
  }

  .ql-editor {
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

    a {
      color: #d5383f;
      text-underline-offset: 3px;
    }

    sup > a {
      color: #d5383f;
      font-weight: 700;
      text-decoration: none;
    }

    hr {
      border: 1px solid #858585;
    }

    pre {
      font-family: 'consolas', 'gulim';
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

      color: #fff;
      font-family: inherit;
      font-weight: inherit;
      font-size: inherit;

      background-color: #333333;
      border-radius: 5px;
    }

    li {
      margin: 10px 0;

      > * {
        margin: 10px 0;
      }
    }

    table {
      width: auto;
      max-width: 900px;
      margin-left: 10px;
      border-collapse: collapse;

      & > thead > tr > th {
        padding: 12px 12px;
        border-bottom: 2px solid #cccccc;
      }

      & > tbody {
        & > tr > td {
          padding: 6px 12px;
          border-bottom: 1px solid #858585;
        }

        & > :last-child > td {
          border-bottom: none;
        }
      }
    }
  }
`;
