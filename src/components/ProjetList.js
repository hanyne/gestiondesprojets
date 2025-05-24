import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaEdit, FaTrash, FaPlus } from 'react-icons/fa';

function ProjetList() {
  const [projets, setProjets] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetch('http://localhost:8080/api/projets')
      .then(res => res.json())
      .then(data => setProjets(data))
      .catch(err => console.error('Erreur:', err));
  }, []);

  const handleEdit = (id) => navigate(`/projets/edit/${id}`);
  const handleDelete = (id) => {
    if (window.confirm('Voulez-vous vraiment supprimer ce projet ?')) {
      fetch(`http://localhost:8080/api/projets/${id}`, { method: 'DELETE' })
        .then(res => {
          if (res.ok) setProjets(projets.filter(projet => projet.id !== id));
        })
        .catch(err => console.error('Erreur:', err));
    }
  };
  const handleAdd = () => navigate('/projets/add');

  return (
    <div className="container mx-auto p-6">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-3xl font-bold text-gray-800">Liste des Projets</h2>
        <button
          onClick={handleAdd}
          className="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 flex items-center"
        >
          <FaPlus className="mr-2" /> Ajouter Projet
        </button>
      </div>
      <div className="overflow-x-auto">
        <table className="min-w-full bg-white shadow-md rounded-lg overflow-hidden">
          <thead className="bg-gray-800 text-white">
            <tr>
              <th className="py-3 px-4 text-left">ID</th>
              <th className="py-3 px-4 text-left">Nom</th>
              <th className="py-3 px-4 text-left">Date Debut</th>
              <th className="py-3 px-4 text-left">Date Fin</th>
              <th className="py-3 px-4 text-left">Budget</th>
              <th className="py-3 px-4 text-left">Statut</th>
              <th className="py-3 px-4 text-left">Actions</th>
            </tr>
          </thead>
          <tbody>
            {projets.map(projet => (
              <tr key={projet.id} className="border-b hover:bg-gray-100">
                <td className="py-3 px-4">{projet.id}</td>
                <td className="py-3 px-4">{projet.nom}</td>
                <td className="py-3 px-4">{projet.dateDebut}</td>
                <td className="py-3 px-4">{projet.dateFin || 'N/A'}</td>
                <td className="py-3 px-4">{projet.budget.toFixed(2)} €</td>
                <td className="py-3 px-4">{projet.statut}</td>
                <td className="py-3 px-4">
                  <button
                    onClick={() => handleEdit(projet.id)}
                    className="mr-2 text-blue-500 hover:text-blue-700"
                  >
                    <FaEdit />
                  </button>
                  <button
                    onClick={() => handleDelete(projet.id)}
                    className="text-red-500 hover:text-red-700"
                  >
                    <FaTrash />
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default ProjetList;