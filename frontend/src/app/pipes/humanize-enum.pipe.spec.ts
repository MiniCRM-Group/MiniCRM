import { HumanizeEnumPipe } from './humanize-enum.pipe';

describe('HumanizeEnumPipe', () => {
  const pipe: HumanizeEnumPipe = new HumanizeEnumPipe();

  it('should create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  describe('Bad Inputs', () => {
    it('should return null when given null', () => {
      expect(pipe.transform(null)).toEqual(null);
    });

    it('should return the given non-string object', () => {
      const actual = pipe.transform({test: 'test'} as any);
      expect(actual).toEqual({test: 'test'} as any);
    });

    it('should return undefined when given undefined', () => {
      expect(pipe.transform(undefined)).toEqual(undefined);
    });
  });

  describe('Humanize String Inputs', () => {
    it('should humanize NEW', () => {
      expect(pipe.transform('NEW')).toEqual('New');
    });

    it('should humanize HELLO_THERE', () => {
      expect(pipe.transform('HELLO_THERE')).toEqual('Hello there');
    });

    it('should not change already humanized strings', () => {
      expect(pipe.transform('I am already readable')).toEqual('I am already readable');
    });
  });
});
