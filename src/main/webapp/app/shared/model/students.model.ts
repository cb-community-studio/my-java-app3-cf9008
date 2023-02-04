export interface IStudents {
  id?: string;
  name?: string;
  email?: string;
  age?: number | null;
}

export const defaultValue: Readonly<IStudents> = {};
