import styled from 'styled-components';

export const EditorWrapper = styled.div`
  position: relative;
  min-width: 340px;
  max-width: 960px;

  margin: 0 auto;

  background-color: #333;

  .ql-toolbar .ql-stroke {
    fill: none;
    stroke: #fff;
  }

  .ql-toolbar .ql-fill {
    fill: #fff;
    stroke: none;
  }

  .ql-toolbar .ql-picker {
    color: #fff;
  }

  .ql-toolbar,
  .ql-container {
    border: none;
  }

  .ql-editor {
    color: #fff;
  }
`;
