import React from "react";
import "./HintTile.css";

export default function HintTile({ label, value, status }) {
  return (
    <div className="hint-tile">
      <div className="hint-label">{label}</div>
      <div className={`hint-value ${status || "red"}`}>
        {Array.isArray(value) ? value.join(", ") : (value ?? "-")}
      </div>
    </div>
  );
}
