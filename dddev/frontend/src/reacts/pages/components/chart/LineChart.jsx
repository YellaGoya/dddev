import { ResponsiveLine } from '@nivo/line';

const LineChart = ({ data /* see data tab */ }) => (
  <ResponsiveLine
    enableArea
    enablePointLabel
    areaBaselineValue={21}
    data={data}
    theme={{
      text: {
        fontSize: 11,
        fill: 'var(--font-rev)',
        outlineWidth: 3,
        outlineColor: 'transparent',
      },
      axis: {
        ticks: {
          text: {
            fontSize: 12,
            fill: 'var(--font-rev)',
            outlineWidth: 0,
            outlineColor: 'transparent',
          },
        },
      },
      grid: {
        line: {
          stroke: 'var(--thumb-hover)',
          strokeWidth: 1,
        },
      },
    }}
    curve="catmullRom"
    margin={{ top: 30, right: 45, bottom: 50, left: 40 }}
    xScale={{ type: 'point' }}
    yScale={{
      type: 'linear',
      min: 'auto',
      max: 'auto',
      stacked: true,
      reverse: false,
    }}
    yFormat=" >-"
    enableCrosshair={false}
    axisTop={null}
    axisRight={null}
    axisBottom={{
      tickSize: 5,
      tickPadding: 5,
      tickRotation: 0,
    }}
    axisLeft={null}
    colors={{ scheme: 'blues' }}
    pointLabelYOffset={-12}
    pointSize={10}
    pointColor={{ theme: 'background' }}
    pointBorderWidth={2}
    pointBorderColor={{ from: 'serieColor' }}
  />
);

export default LineChart;
