import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Students from './students';
import StudentsDetail from './students-detail';
import StudentsUpdate from './students-update';
import StudentsDeleteDialog from './students-delete-dialog';

const StudentsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Students />} />
    <Route path="new" element={<StudentsUpdate />} />
    <Route path=":id">
      <Route index element={<StudentsDetail />} />
      <Route path="edit" element={<StudentsUpdate />} />
      <Route path="delete" element={<StudentsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StudentsRoutes;
