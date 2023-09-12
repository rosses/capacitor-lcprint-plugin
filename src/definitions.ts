export interface LcprintPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
