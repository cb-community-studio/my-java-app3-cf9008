import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './students.reducer';

export const StudentsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const studentsEntity = useAppSelector(state => state.students.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="studentsDetailsHeading">Students</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{studentsEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{studentsEntity.name}</dd>
          <dt>
            <span id="email">Email</span>
          </dt>
          <dd>{studentsEntity.email}</dd>
          <dt>
            <span id="age">Age</span>
          </dt>
          <dd>{studentsEntity.age}</dd>
        </dl>
        <Button tag={Link} to="/students" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/students/${studentsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default StudentsDetail;
