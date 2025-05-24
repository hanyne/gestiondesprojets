import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

function RessourceList() {
  const [ressources, setRessources] = useState([]);

  useEffect(() => {
    fetch('http://localhost:8080/api/ressources')
      .then(response => response.json())
      .then(data => setRessources(data))
      .catch(error => console.error('Error fetching ressources:', error));
  }, []);

  const handleDelete = (id) => {
    if (window.confirm('Êtes-vous sûr de vouloir supprimer cette ressource ?')) {
      fetch(`http://localhost:8080/api/ressources/${id}`, {
        method: 'DELETE',
      })
        .then(() => setRessources(ressources.filter(ressource => ressource.id !== id)))
        .catch(error => console.error('Error deleting ressource:', error));
    }
  };

  return (
    <div>
      <h2 className="text-2xl font-semibold mb-4">Liste des Ressources</h2>
      <Link to="/ressources/new" className="button">Ajouter Ressource</Link>
      <ul>
        {ressources.map(ressource => (
          <li key={ressource.id}>
            {ressource.nom} - {ressource.type} (Coût: {ressource.cout}€, Disponible: {ressource.disponibilite ? 'Oui' : 'Non'})
            <Link to={`/ressources/edit/${ressource.id}`} className="ml-4">Modifier</Link>
            <button onClick={() => handleDelete(ressource.id)} className="delete ml-4">Supprimer</button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default RessourceList;