import { HumanizeEnumPipe } from './humanize-enum.pipe';

describe('HumanizeEnumPipe', () => {
  it('create an instance', () => {
    const pipe = new HumanizeEnumPipe();
    expect(pipe).toBeTruthy();
  });
});
