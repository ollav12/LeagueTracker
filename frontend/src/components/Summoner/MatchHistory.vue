<template>
  <div class="match-history">
    <h3>Last 20 Matches</h3>
    <table>
      <thead>
        <tr>
          <th>Match ID</th>
          <th>Champion</th>
          <th>Result</th>
          <th>K/D/A</th>
          <th>Duration</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="match in matches" :key="match.matchId">
          <td>{{ match.matchId }}</td>
          <td>{{ match.champion }}</td>
          <td>{{ match.result }}</td>
          <td>{{ match.kda }}</td>
          <td>{{ match.duration }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  props: {
    matches: Array,
  },
};
</script>

<style scoped>
.match-history {
  margin-top: 20px;
  background-color: #f7f7f9; /* Light grey background */
  padding: 16px; /* Add padding for better spacing */
  border-radius: 4px; /* Optional: rounded corners */
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  border: 1px solid #ddd;
  padding: 8px;
}

th {
  background-color: #f2f2f2;
  text-align: left;
}
</style>
