import { useEffect, useState } from "react";
import { SaltAndPepper } from "../api/saltAndPepper.js";

export default function UsersList() {
  const [users, setUsers] = useState([]);
  const [page, setPage] = useState(0);
  const [limit, setLimit] = useState(50);

  useEffect(() => {
    SaltAndPepper.getAllUsers(page, limit).then((result) => {
      setUsers(result.users);
    });
  }, [page, limit]);

  return (
    <div className="users-list">
      <div className="pagination-controls">
        <button onClick={() => setPage(page - 1)} disabled={page === 0}>Previous</button>
        <span>Page: {page + 1}</span>
        <button onClick={() => setPage(page + 1)}>Next</button>
        <select value={limit} onChange={(e) => setLimit(Number(e.target.value))}>
          <option value={10}>10</option>
          <option value={20}>20</option>
          <option value={50}>50</option>
          <option value={100}>100</option>
        </select>
      </div>
      {users.map((user) => (
        <div key={user.id} className="user-item">
          <div className="user-name">{user.name}</div>
          <div className="user-role">{user.role}</div>
        </div>
      ))}
    </div>
  );
}
