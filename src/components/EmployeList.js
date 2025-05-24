import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

function EmployeList() {
  const [employes, setEmployes] = useState([]);

  useEffect(() => {
    fetch('http://localhost:8080/api/employes')
      .then(response => response.json())
      .then(data => setEmployes(data))
      .catch(error => console.error('Error fetching employes:', error));
  }, []);

  const handleDelete = (id) => {
    if (window.confirm('Êtes-vous sûr de vouloir supprimer cet employé ?')) {
      fetch(`http://localhost:8080/api/employes/${id}`, {
        method: 'DELETE',
      })
        .then(() => setEmployes(employes.filter(employe => employe.id !== id)))
        .catch(error => console.error('Error deleting employe:', error));
    }
  };

  return (
    <div>
      <h2 className="text-2xl font-semibold mb-4">Liste des Employés</h2>
      <Link to="/employes/new" className="button">Ajouter Employé</Link>
      <ul>
        {employes.map(employe => (
          <li key={employe.id}>
            {employe.nom} - {employe.role} ({employe.equipe})
            <Link to={`/employes/edit/${employe.id}`} className="ml-4">Modifier</Link>
            <button onClick={() => handleDelete(employe.id)} className="delete ml-4">Supprimer</button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default EmployeList;