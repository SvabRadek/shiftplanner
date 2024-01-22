type Props = {
  title: string
}

export function NameCell(props: Props) {
  return (
    <div style={{
      display: "flex",
      userSelect: "none",
      width: 200,
      height: 50,
      border: "solid",
      borderColor: "var(--lumo-tint-20pct)",
      borderWidth: "1px",
      justifyContent: "start",
      alignItems: "center",
      backgroundColor: "var(--lumo-shade-5pct)",
      paddingLeft: 10
    }}>
      {props.title}
    </div>
  );
}
