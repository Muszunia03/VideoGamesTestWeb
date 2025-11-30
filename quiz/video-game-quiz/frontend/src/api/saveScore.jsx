const saveScore = async (quizType, username, score) => {
  await fetch("http://localhost:8080/api/results/save", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, score, quizType })
  });
};