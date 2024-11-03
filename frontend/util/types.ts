
export enum Operation {
  ADD,
  DELETE
}

export type PartialBy<T, K extends keyof T> = Omit<T, K> & Partial<Pick<T, K>>

