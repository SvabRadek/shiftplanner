type Props = {
  title: string
  backgroundColor?: string
}

export function GridHeaderCell(props: Props) {
  return (
    <div style={{
      display: "flex",
      userSelect: "none",
      width: 50,
      height: 50,
      border: "solid",
      borderColor: "var(--lumo-tint-20pct)",
      borderWidth: "1px",
      justifyContent: "center",
      alignItems: "center",
      backgroundColor: props.backgroundColor || "var(--lumo-shade-5pct)"
    }}>
      {props.title}
    </div>
  );
}
