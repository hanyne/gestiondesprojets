import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaEdit, FaTrash, FaPlus } from 'react-icons/fa';

function TacheList() {
  const [taches, setTaches] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetch('http://localhost:8080/api/taches')
      .then(res => res.json())
      .then(data => {
        console.log('Fetched tasks:', data);
        setTaches(data);
      })
      .catch(err => console.error('Erreur:', err));
  }, []);

  const handleEdit = (id) => navigate(`/taches/edit/${id}`);
  const handleDelete = (id) => {
    if (window.confirm('Voulez-vous vraiment supprimer cette tâche ?')) {
      fetch(`http://localhost:8080/api/taches/${id}`, { method: 'DELETE' })
        .then(res => {
          if (res.ok) setTaches(taches.filter(tache => tache.id !== id));
        })
        .catch(err => console.error('Erreur:', err));
    }
  };
  const handleAdd = () => navigate('/taches/add');

  return (
    <div className="container mx-auto p-6">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-3xl font-bold text-gray-800">Liste des Taches</h2>
        <button
          onClick={handleAdd}
          className="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 flex items-center"
        >
          <FaPlus className="mr-2" /> Ajouter Tache
        </button>
      </div>
      <div className="overflow-x-auto">
        <table className="min-w-full bg-white shadow-md rounded-lg overflow-hidden">
          <thead className="bg-gray-800 text-white">
            <tr>
              <th className="py-3 px-4 text-left">ID</th>
              <th className="py-3 px-4 text-left">Nom</th>
              <th className="py-3 px-4 text-left">Description</th>
              <th className="py-3 px-4 text-left">Etat</th>
              <th className="py-3 px-4 text-left">Priorite</th>
              <th className="py-3 px-4 text-left">Deadline</th>
              <th className="py-3 px-4 text-left">Projet</th>
              <th className="py-3 px-4 text-left">Responsable</th>
              <th className="py-3 px-4 text-left">Actions</th>
            </tr>
          </thead>
          <tbody>
            {taches.map(tache => (
              <tr key={tache.id} className="border-b hover:bg-gray-100">
                <td className="py-3 px-4">{tache.id}</td>
                <td className="py-3 px-4">{tache.nom}</td>
                <td className="py-3 px-4">{tache.description}</td>
                <td className="py-3 px-4">{tache.etat}</td>
                <td className="py-3 px-4">{tache.priorite}</td>
                <td className="py-3 px-4">{tache.deadline}</td>
                <td className="py-3 px-4">{tache.projet ? tache.projet.nom : 'N/A'}</td>
                <td className="py-3 px-4">{tache.responsable ? `${tache.responsable.nom} (${tache.responsable.role || 'Role non défini'})` : 'N/A'}</td>
                <td className="py-3 px-4">
                  <button
                    onClick={() => handleEdit(tache.id)}
                    className="mr-2 text-blue-500 hover:text-blue-700"
                  >
                    <FaEdit />
                  </button>
                  <button
                    onClick={() => handleDelete(tache.id)}
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

export default TacheList;