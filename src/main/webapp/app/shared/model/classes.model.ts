import dayjs from 'dayjs';

export interface IClasses {
  id?: string;
  className?: string | null;
  time?: string | null;
  teacher?: string | null;
}

export const defaultValue: Readonly<IClasses> = {};
