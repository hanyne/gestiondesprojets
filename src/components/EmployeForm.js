import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { FaSave } from 'react-icons/fa';

function EmployeForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [employe, setEmploye] = useState({
    nom: '',
    role: ''
  });
  const [error, setError] = useState(null);

  const roleOptions = ['Manager', 'Développeur', 'Designer', 'Analyste'];

  useEffect(() => {
    if (id) {
      fetch(`http://localhost:8080/api/employes/${id}`)
        .then(res => res.json())
        .then(data => {
          setEmploye({
            nom: data.nom || '',
            role: data.role || ''
          });
        })
        .catch(err => setError(err.message));
    }
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setEmploye({
      ...employe,
      [name]: value
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setError(null);

    if (!employe.nom) {
      setError("Le nom de l'employé est requis.");
      return;
    }
    if (!employe.role) {
      setError("Le rôle de l'employé est requis.");
      return;
    }

    const method = id ? 'PUT' : 'POST';
    const url = id ? `http://localhost:8080/api/employes/${id}` : 'http://localhost:8080/api/employes';
    const payload = {
      nom: employe.nom,
      role: employe.role
    };

    console.log('Submitting payload:', payload);

    fetch(url, {
      method,
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload)
    })
      .then(res => {
        if (!res.ok) {
          return res.text().then(text => { throw new Error(text || 'Erreur lors de la sauvegarde'); });
        }
        return res.json();
      })
      .then(data => {
        console.log('Employe saved:', data);
        navigate('/employes');
      })
      .catch(err => {
        console.error('Submission error:', err);
        setError(err.message || 'Erreur inattendue lors de la sauvegarde');
      });
  };

  return (
    <div className="max-w-md mx-auto p-6 bg-white shadow-lg rounded-lg">
      <h2 className="text-3xl font-bold mb-6 text-gray-800">{id ? 'Modifier Employé' : 'Ajouter Employé'}</h2>
      {error && <div className="text-red-500 mb-4">{error}</div>}
      <form onSubmit={handleSubmit} className="space-y-6">
        <div>
          <label className="block text-sm font-medium text-gray-700">Nom</label>
          <input
            type="text"
            name="nom"
            value={employe.nom}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
            required
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Rôle</label>
          <select
            name="role"
            value={employe.role}
            onChange={handleChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
            required
          >
            <option value="">Sélectionner un rôle</option>
            {roleOptions.map(option => (
              <option key={option} value={option}>{option}</option>
            ))}
          </select>
        </div>
        <button
          type="submit"
          className="px-4 py-2 bg-green-500 text-white rounded-md hover:bg-green-600 flex items-center"
        >
          <FaSave className="mr-2" /> Sauvegarder
        </button>
      </form>
    </div>
  );
}

export default EmployeForm;